package it.abb;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontHelper {
	
	public static final void setAppFont(ViewGroup mContainer)
	{
		final Typeface mFont = Typeface.createFromAsset(mContainer.getContext().getAssets(),
	    		"fonts/helveticaneueltproroman.ttf"); 
	    
		if (mContainer == null || mFont == null) return;

	    final int mCount = mContainer.getChildCount();

	    // Loop through all of the children.
	    for (int i = 0; i < mCount; ++i)
	    {
	        final View mChild = mContainer.getChildAt(i);
	        if (mChild instanceof TextView)
	        {
	            // Set the font if it is a TextView.
	            ((TextView) mChild).setTypeface(mFont);
	        }
	        else if (mChild instanceof ViewGroup)
	        {
	            // Recursively attempt another ViewGroup.
	            setAppFont((ViewGroup) mChild);
	        }
	    }
	}
}
