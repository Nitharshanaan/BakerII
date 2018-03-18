package com.nitharshanaan.android.baker_ii;

import android.content.Context;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by nitha on 27-02-2018.
 */

@RunWith(AndroidJUnit4.class)
public class StepDetailsActivityTest {

    private static final String NUTELLA_PIE = "Nutella Pie";
    private static final String STEP_ONE_DESCRIPTION = "1. Preheat the oven to 350°F. Butter a 9\" deep dish pie pan.";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<StepDetailsActivity> stepDetailsActivityTestRule =
            new ActivityTestRule<>(StepDetailsActivity.class);

    private IdlingRegistry idlingRegistry;
    private Context context;
    private boolean tablet;

    @Before
    public void registerIdlingResource() {
        context = stepDetailsActivityTestRule.getActivity();
        tablet = context.getResources().getBoolean(R.bool.isTablet);

        //Wait for the query to finish
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void clickNextButton_NextStepIsDisplayed() {
        if (!tablet) {
            onView(withId(R.id.recycler_view_recipes))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.recycler_view_steps))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.button_next_step)).perform(click());

            onView(allOf(isDescendantOfA(withResourceName(context.getString(R.string.action_bar))), withText(NUTELLA_PIE)))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.text_view_step_description)).check(matches(withText(STEP_ONE_DESCRIPTION)));
        }
    }


    @Test
    public void clickPlayerPauseButton_PlayButtonIsDisplayed() {
        if (!tablet) {
            onView(withId(R.id.recycler_view_recipes))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.recycler_view_steps))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.exo_pause))
                    .perform(click());
            onView(withId(R.id.exo_play))
                    .check(matches(isDisplayed()));
        }
    }

}
