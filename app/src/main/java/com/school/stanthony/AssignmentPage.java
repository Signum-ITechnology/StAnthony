package com.school.stanthony;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class AssignmentPage extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    String home,pdf,video,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_page);

        home=getIntent().getExtras().getString("home");
        pdf=getIntent().getExtras().getString("pdf");
        video=getIntent().getExtras().getString("video");
        image=getIntent().getExtras().getString("image");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if(home.equals("0")){
            tabLayout.getTabAt(0).setText("TEXT");
        }else {
            tabLayout.getTabAt(0).setText("TEXT = "+home);
        }

        if(pdf.equals("0")){
            tabLayout.getTabAt(1).setText("PDF");
        }else {
            tabLayout.getTabAt(1).setText("PDF = "+pdf);
        }

        if(video.equals("0")){
            tabLayout.getTabAt(2).setText("VIDEO");
        }else {
            tabLayout.getTabAt(2).setText("VIDEO = "+video);
        }

        if(image.equals("0")){
            tabLayout.getTabAt(3).setText("IMAGE");
        }else {
            tabLayout.getTabAt(3).setText("IMAGE = "+image);
        }


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_assignment_page, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    HomeworkFragment blankFragment1=new HomeworkFragment();
                    return blankFragment1;

                case 1:
                    PdfFragment blankFragment2=new PdfFragment();
                    return blankFragment2;

                case 2:
                    VideoFragment blankFragment3=new VideoFragment();
                    return blankFragment3;

                case 3:
                    ImageFragment blankFragment4=new ImageFragment();
                    return blankFragment4;

            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
