package com.mheredia.petproject


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ContactsRemindersTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun contactsRemindersTest() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.email_edit_text),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.email_textbox),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("test123@test.com"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.password_edit_text),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.password_textbox),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("test123"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.password_edit_text), withText("test123"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.password_textbox),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(pressImeActionButton())

        val materialButton = onView(
            allOf(
                withId(R.id.login_button), withText("Login"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
            allOf(
                withId(R.id.nav_gallery),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val navigationMenuItemView2 = onView(
            allOf(
                withId(R.id.nav_contact),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView2.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.card_title), withText("Melissa"),
                withParent(
                    allOf(
                        withId(R.id.card_text),
                        withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Melissa")))

        val textView2 = onView(
            allOf(
                withId(R.id.card_subtitle), withText("pet sitter"),
                withParent(
                    allOf(
                        withId(R.id.card_text),
                        withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("pet sitter")))

        val imageButton = onView(
            allOf(
                withId(R.id.add_items),
                withParent(
                    allOf(
                        withId(R.id.contact_fragment),
                        withParent(withId(R.id.nav_host_fragment))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val imageButton2 = onView(
            allOf(
                withId(R.id.add_items),
                withParent(
                    allOf(
                        withId(R.id.contact_fragment),
                        withParent(withId(R.id.nav_host_fragment))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton2.check(matches(isDisplayed()))

        val appCompatImageButton3 = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        val navigationMenuItemView3 = onView(
            allOf(
                withId(R.id.nav_reminders),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView3.perform(click())

        val textView3 = onView(
            allOf(
                withId(R.id.card_title), withText("reminder2"),
                withParent(
                    allOf(
                        withId(R.id.card_text),
                        withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("reminder2")))

        val textView4 = onView(
            allOf(
                withId(R.id.card_subtitle), withText("1/19/2021 04:22 AM"),
                withParent(
                    allOf(
                        withId(R.id.card_text),
                        withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("1/19/2021 04:22 AM")))

        val imageButton3 = onView(
            allOf(
                withId(R.id.add_items),
                withParent(
                    allOf(
                        withId(R.id.contact_fragment),
                        withParent(withId(R.id.nav_host_fragment))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton3.check(matches(isDisplayed()))

        val imageButton4 = onView(
            allOf(
                withId(R.id.add_items),
                withParent(
                    allOf(
                        withId(R.id.contact_fragment),
                        withParent(withId(R.id.nav_host_fragment))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton4.check(matches(isDisplayed()))

        val recyclerView = onView(
            allOf(
                withId(R.id.list_items),
                childAtPosition(
                    withId(R.id.list_container),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val recyclerView2 = onView(
            allOf(
                withId(R.id.list_items),
                childAtPosition(
                    withId(R.id.list_container),
                    0
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatImageView = onView(
            allOf(
                withId(R.id.cancel_dialog),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.custom),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.cancel_dialog),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.custom),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())

        val recyclerView3 = onView(
            allOf(
                withId(R.id.list_items),
                childAtPosition(
                    withId(R.id.list_container),
                    0
                )
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val textView5 = onView(
            allOf(
                withId(R.id.reminder_date), withText("1/19/2021"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("1/19/2021")))

        val editText = onView(
            allOf(
                withId(R.id.reminder_name), withText("reminder2"),
                withParent(withParent(withId(R.id.reminder_name_layout))),
                isDisplayed()
            )
        )
        editText.check(matches(withText("reminder2")))

        val textView6 = onView(
            allOf(
                withId(R.id.reminder_time), withText("04:22 AM"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("04:22 AM")))

        val button = onView(
            allOf(
                withId(R.id.save_reminder), withText("SAVE"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.cancel_dialog),
                withParent(withParent(withId(R.id.custom))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView7 = onView(
            allOf(
                withId(R.id.alertTitle), withText("Edit Reminder"),
                withParent(
                    allOf(
                        withId(R.id.title_template),
                        withParent(withId(R.id.topPanel))
                    )
                ),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("Edit Reminder")))

        val textView8 = onView(
            allOf(
                withId(R.id.alertTitle), withText("Edit Reminder"),
                withParent(
                    allOf(
                        withId(R.id.title_template),
                        withParent(withId(R.id.topPanel))
                    )
                ),
                isDisplayed()
            )
        )
        textView8.check(matches(withText("Edit Reminder")))

        val materialButton2 = onView(
            allOf(
                withId(R.id.save_reminder), withText("Save"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val appCompatImageButton4 = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())

        val navigationMenuItemView4 = onView(
            allOf(
                withId(R.id.nav_gallery),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView4.perform(click())

        val appCompatImageButton5 = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton5.perform(click())

        val navigationMenuItemView5 = onView(
            allOf(
                withId(R.id.logout),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView5.perform(click())

        val textView9 = onView(
            allOf(
                withText("LoginActivity"),
                withParent(
                    allOf(
                        withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView9.check(matches(withText("LoginActivity")))

        val textView10 = onView(
            allOf(
                withText("LoginActivity"),
                withParent(
                    allOf(
                        withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView10.check(matches(withText("LoginActivity")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
