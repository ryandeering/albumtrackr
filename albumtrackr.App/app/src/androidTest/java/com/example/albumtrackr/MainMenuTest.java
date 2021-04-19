package com.example.albumtrackr;


import android.view.View;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainMenuTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction view = onView(
                allOf(withId(android.R.id.statusBarBackground),
                        withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)),
                        isDisplayed()));
        view.check(matches(isDisplayed()));

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.action_bar),
                        withParent(allOf(withId(R.id.action_bar_container),
                                withParent(withId(R.id.decor_content_parent)))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withText("albumtrackr"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        textView.check(matches(withText("albumtrackr")));

        ViewInteraction linearLayout = onView(
                allOf(withContentDescription("My Album lists"),
                        withParent(withParent(withId(R.id.tablayout_id))),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withText("MY ALBUM LISTS"),
                        withParent(allOf(withContentDescription("My Album lists"),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView2.check(matches(withText("MY ALBUM LISTS")));

        ViewInteraction linearLayout2 = onView(
                allOf(withContentDescription("Popular Album Lists"),
                        withParent(withParent(withId(R.id.tablayout_id))),
                        isDisplayed()));
        linearLayout2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withText("POPULAR ALBUM LISTS"),
                        withParent(allOf(withContentDescription("Popular Album Lists"),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView3.check(matches(withText("POPULAR ALBUM LISTS")));

        ViewInteraction linearLayout3 = onView(
                allOf(withContentDescription("Newest Album Lists"),
                        withParent(withParent(withId(R.id.tablayout_id))),
                        isDisplayed()));
        linearLayout3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withText("NEWEST ALBUM LISTS"),
                        withParent(allOf(withContentDescription("Newest Album Lists"),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView4.check(matches(withText("NEWEST ALBUM LISTS")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.fab_add_list),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction linearLayout4 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                        withParent(withId(R.id.decor_content_parent)))),
                        isDisplayed()));
        linearLayout4.check(matches(isDisplayed()));
    }
}
