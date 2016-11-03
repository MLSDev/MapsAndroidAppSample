package com.mlsdev.mapsappsample;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> rule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testStartPlacePickerActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_pick_a_place))
                .perform(ViewActions.click());

        Intents.intended(IntentMatchers.hasComponent(PlacePickerActivity.class.getName()));
    }

}
