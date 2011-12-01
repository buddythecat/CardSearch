package com.danceswithcaterpillars.cardsearch.activities;

import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.danceswithcaterpillars.cardsearch.content.local.db.CardDatabaseConstants.*;

import com.danceswithcaterpillars.cardsearch.R;
import com.danceswithcaterpillars.cardsearch.content.image.GetSetImgTask;
import com.danceswithcaterpillars.cardsearch.content.local.CardDatabaseProvider;
import com.danceswithcaterpillars.cardsearch.model.Card;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * EditCardInDeckActivity - 
 * this class is responsible for letting a user edit the quantity of a card in the
 * deck, as well as letting the user view the card details. 
 * 
 * @author Rich "Dances With Caterpillars" Tufano
 *
 */
public class EditCardInDeckActivity extends Activity{
	/** TAG - the class name for logging */
	private static final String TAG = "EditCardInDeckActivity";
	
	/** RESULT_DONE - the Code for no-action */
	public static final int RESULT_DONE = 10;
	/** RESULT_DELTE - the code that card was deleted */
	public static final int RESULT_DELETE = 11;
	/** RESULT_UPDATE - the code that card was update */
	public static final int RESULT_UPDATE = 12;
	
	/** focusedCard - the card being viewed */
	private Card focusedCard;
	/** fwipper - the viewflipper that switches between the card image and the card details */
	private ViewFlipper fwipper;
	/** gestureDetector - the GestureDetector responsible for detecting the swype that switches the viewflipper */
	private GestureDetector gestureDetector;
	/** gestureListener - the GestureListener responsible for listening for gestures */
	private OnTouchListener gestureListener;
	/** cardName, cardType, cardRule, cardPow, countSliderLabel - the TextViews for these details of the card */
	private TextView cardName, cardType, cardRule, cardPow, countSliderLabel;
	/** manaLayout - the LinearLayout responsible for storing the manaLayout */
	private LinearLayout manaLayout;
	/** cardImage, setImage - the imageViews storing the setImage and the cardImage */
	private ImageView cardImage, setImage;
	/** cardImageProgress - the ProgressBar that spins while the cardImage is loading */
	private ProgressBar cardImageProgress;
	/** countSlider - the SeekBar that lets a user adjust how many cards are in the deck */
	private SeekBar countSlider;
	
	/**
	 * EditCardSwipeGesture - 
	 * this class is the listener to detect if the user makes a SwipeGesture,
	 * which is used to flip through the ViewFlipper.  The gesture is a simple one,
	 * that is basically a swipe to the left, or a swipe to the right.
	 * 
	 * This gesture is based upon (what seems to be) a very generic swipe gesture found online
	 * 
	 * @author Rich "Dances With Caterpillars" Tufano
	 *
	 */
	class EditCardSwipeGesture extends SimpleOnGestureListener {
		/** SWIPE_MIN_DISTANCE - the minimum distance that constitutes the gesture */
		private static final int SWIPE_MIN_DISTANCE = 100;
		/** SWIPE_MAX_OFF_PATH - the maximum tolerance for the swipe to deviate from the ideal swipe. This is important 
		 * because the we want the tolerance to be low enough that if the user is just messing around with their fingers they
		 * dont trigger the gesture, but high enough that user doesn't have to be perfect */
		private static final int SWIPE_MAX_OFF_PATH = 550;
		/** SWIPE_THRESHOLD_VELOCITY - the minimum velocity that a user must swipe with that will trigger the gesture */
		private static final int SWIPE_THRESHOLD_VELOCITY = 100;
		
		/*
		 * onFling - 
		 * This method is used to base the motion event.  The parameters are :
		 * 	1) MotionEvent evnt1 - the starting event (where the user started the swipe)
		 * 	2) MotionEvent evnt2 - the ending event (where the user ended the swipe)
		 * 	3) velocityY - the velocity of the motion in the Y direction
		 * 	4) velocityX - the volicty of the motion in the X direction
		 * view 
		 * The method then tests the tolerances and directions of the motion to make sure it conforms to the gesture,
		 * and will then trigger the events associated with the gesture (e.g. flipping through the ViewFlipper).
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
		 */
		@Override
		public boolean onFling(MotionEvent evnt1, MotionEvent evnt2, float velocityY, float velocityX){
			//Simple logging: let us know that the event's been fired
			Log.i(TAG, "Fwining!");
			//check to make sure that the swipe doesn't toggle the maximum deviation case (going diagonal)
			if(Math.abs(evnt1.getY()-evnt2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			//If the X from event 1 is greater then the X from event 2, the finger moved left, and we're fwipping backwards.
			if((evnt1.getX() - evnt2.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
				//set the animation for the flipper to move the current view out to the left, and the new view in from the right
				fwipper.setInAnimation(makeInFromRightAnimation());
				fwipper.setOutAnimation(makeOutToLeftAnimation());
				//tell the flipper to show the next view(view to the right)
				fwipper.showNext();
			}
			//if the X from event 2 is greater then the X from event 1, the finger moved to the right, and we're fwipping forwards.
			else if((evnt2.getX() - evnt1.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
				//set the animation for the flipper to move the current view out to the right, and the new view in from the left
				fwipper.setInAnimation(makeInFromLeftAnimation());
				fwipper.setOutAnimation(makeOutToRightAnimation());
				//tell the flipper to show the previous view (view to the left)
				fwipper.showPrevious();
			}
			//Call the SimpleGestureListener's onFling method
			return super.onFling(evnt1, evnt2, velocityX, velocityY);
		}
	}
	
	/**
	 * makeInFromRightAnimation - 
	 * build an Animation for the ViewFlipper to slide the View into the screen
	 * from the right.  
	 * 
	 * The code for this Animation is fairly generic, and causes the view to slide in at a constant rate.
	 * @return Animation that will translate the view to move in a positive X direction at a constant rate
	 */
	private Animation makeInFromRightAnimation(){
		/*
		 * Create the animation.  RELATIVE_TO_PARENT specifies that the translation in that direction should be
		 * measured relative to it's parent's position.
		 */
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f, 	//move +1.0 in x direction From the Parent View
				Animation.RELATIVE_TO_PARENT, 0.0f, 	//move 0.0 in x direction To the parent View
				Animation.RELATIVE_TO_PARENT, 0.0f, 	//move 0.0 in y direction from the parent view
				Animation.RELATIVE_TO_PARENT, 0.0f);	//move 0.0 in y direction to the parent view
		//set the duration of the animation to 200 millis
		inFromRight.setDuration(200);
		//set the interpolator for the animation to the Acceleration Interpolator
		inFromRight.setInterpolator(new AccelerateInterpolator());
		//return the animation
		return inFromRight;
	}
	
	/**
	 * makeOutFromLeftAnimation - 
	 * build an Animation for the ViewFlipper to slide the View off the screen
	 * to the left.  
	 * 
	 * The code for this Animation is fairly generic, and causes the view to slide in at a constant rate.
	 * @return Animation that will translate the view to move in a negative X direction at a constant rate
	 */
	private Animation makeOutToLeftAnimation(){
		Animation outFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f, 	//move 0.0 in x direction from the parent view
				Animation.RELATIVE_TO_PARENT, -1.0f, 	//move -1.0 in x direction to the parent view
				Animation.RELATIVE_TO_PARENT, 0.0f, 	//move 0.0 in y direction from parent view
				Animation.RELATIVE_TO_PARENT, 0.0f);	//move 0.0 in y direction to parent view
		//set duration for 200 millis
		outFromLeft.setDuration(200);
		//set interpolator to the AccelerateInterpolator
		outFromLeft.setInterpolator(new AccelerateInterpolator());
		//return the animation
		return outFromLeft;
	}
	
	/**
	 * makeInFromLeftAnimation - 
	 * build an Animation for the ViewFlipper to slide the View into the screen
	 * from the left.  
	 * 
	 * The code for this Animation is fairly generic, and causes the view to slide in at a constant rate.
	 * @return Animation that will translate the view to move in a negative X direction at a constant rate
	 */
	private Animation makeInFromLeftAnimation(){
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,	//move -1.0 in x direction from the parent view 
				Animation.RELATIVE_TO_PARENT, 0.0f, 	//move 0.0 in x direction to the parent view
				Animation.RELATIVE_TO_PARENT, 0.0f, 	//move 0.0 in y direction from the parent view
				Animation.RELATIVE_TO_PARENT, 0.0f);	//move 0.0 in y direction to the parent view
		//set duration to 200 millis
		inFromLeft.setDuration(200);
		//set interpolator
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		//return the animation
		return inFromLeft;
	}
	
	/**
	 * makeOutToRightAnimation - 
	 * build an Animation for the ViewFlipper to slide the View off the screen
	 * from the right.  
	 * 
	 * The code for this Animation is fairly generic, and causes the view to slide in at a constant rate.
	 * @return Animation that will translate the view to move in a positive X direction at a constant rate
	 */
	private Animation makeOutToRightAnimation(){
		Animation outToRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,		//move 0.0 in x direction from the parent view 
				Animation.RELATIVE_TO_PARENT, +1.0f, 	//move -1.0 in x direction to the parent view
				Animation.RELATIVE_TO_PARENT, 0.0f, 	//move 0.0 in y direction from the parent view
				Animation.RELATIVE_TO_PARENT, 0.0f);	//move 0.0 in y direction to parent view
		//set duration to 200 millis
		outToRight.setDuration(200);
		//set interpolation
		outToRight.setInterpolator(new AccelerateInterpolator());
		//return animation
		return outToRight;
	}
	
	/*
	 * onCreate - 
	 * initialize the Activity and it's views, bind the gestureDetectors, and gestureListeners
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.edit_card_from_deck);
		//set the focused card from the intent's extras
		focusedCard = this.getIntent().getExtras().getParcelable("Card");
		//bind the ViewFlipper to it's local instance variable
		fwipper = (ViewFlipper)this.findViewById(R.id.edit_card_flipper);
		//bind the GestureDetector
		gestureDetector = new GestureDetector(this, new EditCardSwipeGesture());
		/*
		 * set the Activity's gestureListener to a new touch listener.
		 * This has the Activity check the touch even against the gestureDetector.
		 */
		setGestureListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//if the gestureDetector finds that the vent was an EditCardSwipeGesture it returns true
				if(gestureDetector.onTouchEvent(event)){
					return true;
				}
				//if not it returns false.
				else
					return false;
			}
		});
		//initialize the views
		this.initViews();
		//fill the card's details into the views
		this.fillDetails();
	}
	
	/**
	 * initViews() - 
	 * Initialize all of the views associated with the card detail viewer/editor
	 */
	private void initViews(){
		//bind the text views
		cardName = (TextView)this.findViewById(R.id.edit_card_details_cardname);
		cardType = (TextView)this.findViewById(R.id.edit_card_details_cardType);
		cardRule = (TextView)this.findViewById(R.id.edit_card_details_rule);
		cardPow = (TextView)this.findViewById(R.id.edit_card_details_powTough);
		//bind the manaLayout
		manaLayout = (LinearLayout)this.findViewById(R.id.edit_card_details_cost);
		//bind the cardImage to the ImageView
		cardImage = (ImageView)this.findViewById(R.id.edit_card_details_cardimg);
		//bind the setImage and the progress bar
		setImage = (ImageView)this.findViewById(R.id.edit_card_details_setImg);
		cardImageProgress = (ProgressBar)this.findViewById(R.id.edit_card_details_imgprogress);
		//bind the slider and set it up
		countSliderLabel = (TextView)this.findViewById(R.id.edit_card_details_qtyLabel);
		countSliderLabel.setText("Quantity: "+focusedCard.getQuantity());
		countSlider = (SeekBar)this.findViewById(R.id.edit_card_details_qty);
		countSlider.setMax(4);
		/*
		 * For the slider, we have to bind a new OnSeekBarChangeListener to the Slider.  This
		 * change listener should be used to update the text view associated with it.
		 */
		countSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			//this method's not used (we're not actively tracking)
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//focusedCard.setQuantity(seekBar.getProgress());
			}
			//this method's not used (we're not actively tracking)
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			/*
			 * onProgressChanged - 
			 * this should update the label next to the seekbar with the total number of cards in the deck.
			 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//create a new strinbuilder with the text Quantity
				StringBuilder builder = new StringBuilder("Quantity: ");
				//append the amount of cards
				builder.append(progress);
				//set the label text as the builder's value
				countSliderLabel.setText(builder);
			}
		});
	}
	
	/**
	 * fillDetails - 
	 * This method is in charge of filling the details of the card, including
	 * the card name, the type,
	 * the rule, the mana cost, the set, the power/toughness OR loyalty, 
	 * and the quantity of the card in the deck. 
	 */
	private void fillDetails(){
		//fill the card name
		cardName.setText((CharSequence)focusedCard.getName());
		
		//fill the manaCost by calling the card's setatic method with the cost
		String manaCost = focusedCard.getCost();
		manaLayout.removeAllViews();
		Card.buildManaBar(new WeakReference<LinearLayout>(manaLayout), manaCost);
		
		//fill the power and toughness OR Loyalty of a plainswalker
		String pow = focusedCard.getPower();
		//case for a creature
		if(!pow.equals("")){
			cardPow.setVisibility(View.VISIBLE);
			pow += "/"+focusedCard.getToughness();
			cardPow.setText(pow);
		}
		//Case for a planeswalker
		else if(!focusedCard.getLoyalty().equals("")){
			cardPow.setVisibility(View.VISIBLE);
			pow = "Loyalty: ";
			pow += focusedCard.getLoyalty();
			cardPow.setText(pow);
		}
		//case for an instant or sorcery
		else{
			cardPow.setVisibility(View.GONE);
		}
		
		//fill the card's rule.
		String rule = focusedCard.getRule();
		//no rule, clear the visibility of the rule's TextView
		if(rule.length()==0 || rule==null){
			cardRule.setVisibility(View.GONE);
		}
		//we have a rule, fill it using the Card's static method Card.buildCardRuleWithImages();
		else{
			cardRule.setVisibility(View.VISIBLE);
			cardRule.setText(Card.buildCardRulesWithImages(rule, getApplicationContext()));	
		}
		
		//set card's type
		cardType.setText((CharSequence)focusedCard.getFullType());
		
		//log the quantity of card for testing
		Log.i(TAG, "Qty of card: "+focusedCard.getQuantity());
		//set the progress of the slider to the amount of cards in the deck
		countSlider.setProgress(focusedCard.getQuantity());
		
		//get the setInfo from the deck (as a JSONArray), to ge tthe Card Image and Set Image
		JSONArray setInfo = focusedCard.getSetInfo();
		//if we have setInfo
		if(setInfo.length()>0){
			try{
				//get the first package of setInfo
				JSONObject firstSet = setInfo.getJSONObject(0);
				/*
				 * create an array of Strings that hold the setInfo. The array should be structured as so:
				 * setInfo[i][j] 
				 * 	i - where i represents the amount of sets associated with this card
				 *  j = 0 - the setCode (unique identifying string for the set)
				 *  j = 1 - the rarity of the card within this set
				 * 	j = 2 - the card's number within the set, used to identify the card
				 */
				String[][] setInfos = new String[setInfo.length()][3];
				//build the setInfos for each set associated with this card
				for(int i = 0; i<setInfo.length(); i++){
					//make a JSONObject for this set
					firstSet = setInfo.getJSONObject(i);
					//assign the setCode to the array
					setInfos[i][0] = firstSet.getString("setcode");
					//assign the rarity to the array
					setInfos[i][1] = firstSet.getString("rarity");
					//assign the cardNumber to the array
					setInfos[i][2] = firstSet.getString("number");
				}
				if(focusedCard.getMulti()==null)
					setInfos[0][2]+="a";
				//Create a GetSetImageTask to retrieve the image for this task, and pass to it the reference to the setImage, and the array containing setInfo
				GetSetImgTask retriever = new GetSetImgTask(setImage, null, setInfos);
				//Execute the thread, specifying that we want to have the set image returned
				retriever.execute(new String[]{GetSetImgTask.SET_IMG});
				
				//Create a GetSetImageTask to retrieve the image for the card, and pass to it the reference to the cardImage, the ProgressBar for the cardImage, and the setInfo
				retriever = new GetSetImgTask(cardImage, cardImageProgress, setInfos);
				//Execute the thread, specifying that we ant to have it return the cardImage
				retriever.execute(new String[]{GetSetImgTask.CARD_IMG});
			}catch(JSONException e){
				//If there was an error parsing the JSON associated with the card's sets: throw an error
				Log.e(TAG, "Error parsing set info", e);
			}
		}
	}
	
	/**
	 * finishEditing - 
	 * This method is called with the user clicks the "Finished" button.  The method
	 * checks to see if the card has been updated (currently only the quantity can be updated).
	 * If it has, we update the card in the database, and return the status back to the last Activity on the stack.
	 * if the card's quantity was set to 0, we delete the card from the database, where the card is bound by it's DeckId and it's name.
	 * If no update has been made, we return the status back to the activity
	 * @param v - the view that this method was called from.
	 */
	public void finishEditing(View v){
		if(focusedCard.getQuantity() != countSlider.getProgress()){
			//update the card, qty has changed
			if(countSlider.getProgress()==0){
				//remove the card, qty is down to zero
				getContentResolver().delete(CardDatabaseProvider.CONTENT_URI,_ID+"=?",new String[]{String.valueOf(focusedCard.getId())});
				this.setResult(RESULT_DELETE);
			}
			else{
				//simply update the quantity
				focusedCard.setQuantity(countSlider.getProgress());
				ContentValues values = new ContentValues();
				values.put(QUANTITY, focusedCard.getQuantity());
				int updated = getContentResolver().update(CardDatabaseProvider.CONTENT_URI,values,_ID+"=?",new String[]{String.valueOf(focusedCard.getId())});
				//if the amount of cards updated is returned as 0, we've failed
				if(updated == 0){
					 Log.d(TAG, "Failure! Did not insert card to database");
				 }
				//if not, we've successfuly removed the cards
				else{
					Log.d(TAG, "Updated a total of "+updated+" rows");
					this.setResult(RESULT_UPDATE);
				}
			}
		}
		//no cards were changed
		else{
			this.setResult(RESULT_DONE);
		}
		//finish the activity, which will pop it off the stack and send the result back to the caller
		this.finish();
	}
	
	/*
	 * onTouchEvent - 
	 * we want to check to make sure that the touchEvent is a our EditCardSwipeGesture, and if it is, trigger that event
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	//check the event
        if (gestureDetector.onTouchEvent(event))
	        return true;
	    else
	    	return false;
    }
    
    /**
     * getGestureListener - 
     * returns the GestureListener for this activity
     * @return gestureListener
     */
	public OnTouchListener getGestureListener() {
		return gestureListener;
	}
	
	/**
	 * setGestureListner - 
	 * sets the GestureListener for this Activity
	 * @param gestureListener the OnTouchListner to bind to.
	 */
	public void setGestureListener(OnTouchListener gestureListener) {
		this.gestureListener = gestureListener;
	}
}
