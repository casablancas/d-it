package com.example.alejandro.d_it;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.*;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    PagerTabStrip tab_strp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //MyPageAdapter myPageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPageAdapter2(getSupportFragmentManager()));
        tab_strp = (PagerTabStrip)findViewById(R.id.tab_strip);
        tab_strp.setTextColor(Color.WHITE);


        /*
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        // Attach the page change listener inside the activity
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });*/
    }

    public class MyPageAdapter2 extends android.support.v4.app.FragmentPagerAdapter {

        Drawable myDrawable;
        private int tabIcons[] = {R.drawable.inicio, R.drawable.citas, R.drawable.perfil};

        public MyPageAdapter2(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {

            switch (position)
            {

                case 0:
                    indexFragment t1 = new indexFragment();
                    return t1;
                case 1:
                    datesFragment t2 = new datesFragment();
                    return t2;
                case 2:
                    profileFragment t3 = new profileFragment();
                    return t3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Locale l = Locale.getDefault();
            SpannableStringBuilder sb = new SpannableStringBuilder(" ");
            SpannableString spannableString = null;

            switch (position)
            {
                case 0:
                    //myDrawable = getResources().getDrawable(R.drawable.img_section1);
                    //use the MrVector library to inflate vector drawable inside tab


                    Drawable drawable = getResources().getDrawable(tabIcons[position]);
                    //set the size of drawable to 36 pixels
                    drawable.setBounds(0, 0, 64, 64);
                    ImageSpan imageSpan = new ImageSpan(drawable);
                    //to make our tabs icon only, set the Text as blank string with white space
                    spannableString = new SpannableString(" ");
                    spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    //myDrawable = getResources().getDrawable(tabIcons[position]);
                    //return "Inicio";
                    return spannableString;
                case 1:
                    Drawable drawable2 = getResources().getDrawable(tabIcons[position]);
                    //set the size of drawable to 36 pixels
                    drawable2.setBounds(0, 0, 64, 64);
                    ImageSpan imageSpan2 = new ImageSpan(drawable2);
                    //to make our tabs icon only, set the Text as blank string with white space
                    spannableString = new SpannableString(" ");
                    spannableString.setSpan(imageSpan2, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    //myDrawable = getResources().getDrawable(tabIcons[position]);
                    //return "Citas";
                    return spannableString;
                case 2:
                    Drawable drawable3 = getResources().getDrawable(tabIcons[position]);
                    //set the size of drawable to 36 pixels
                    drawable3.setBounds(0, 0, 64, 64);
                    ImageSpan imageSpan3 = new ImageSpan(drawable3);
                    //to make our tabs icon only, set the Text as blank string with white space
                    spannableString = new SpannableString(" ");
                    spannableString.setSpan(imageSpan3, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return spannableString;
                    //return "Perfil";
            }
            return null;
        }
    }
}


