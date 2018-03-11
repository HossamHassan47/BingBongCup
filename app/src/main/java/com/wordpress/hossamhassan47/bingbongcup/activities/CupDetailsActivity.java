package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.adapters.CupAdapter;
import com.wordpress.hossamhassan47.bingbongcup.adapters.CupPlayerAdapter;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Cup;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;

import java.util.List;

public class CupDetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private int cupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cup_details);

        int numberOfPages = getIntent().getIntExtra("numberOfPages", 2);

        cupId = getIntent().getIntExtra("cupId", -1);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), numberOfPages);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pagerCupDetails);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final int _numberOfPages;

        public SectionsPagerAdapter(FragmentManager fm, int numberOfPages) {

            super(fm);
            _numberOfPages = numberOfPages;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return _numberOfPages;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Players";
            } else if (position == (_numberOfPages - 1)) {
                return "Final";
            } else if (position == (_numberOfPages - 2)) {
                return "3rd";
            } else {
                return getRoundName(position);
            }
        }

        private String getRoundName(int position) {
            if (_numberOfPages == 8) {
                if (position == 1) {
                    return "Round(64)";
                } else if (position == 2) {
                    return "Round(32)";
                } else if (position == 3) {
                    return "Round(16)";
                } else if (position == 4) {
                    return "Round(08)";
                } else if (position == 5) {
                    return "Round(04)";
                }
            } else if (_numberOfPages == 7) {
                if (position == 1) {
                    return "Round(32)";
                } else if (position == 2) {
                    return "Round(16)";
                } else if (position == 3) {
                    return "Round(08)";
                } else if (position == 4) {
                    return "Round(04)";
                }
            } else if (_numberOfPages == 6) {
                if (position == 1) {
                    return "Round(16)";
                } else if (position == 2) {
                    return "Round(08)";
                } else if (position == 3) {
                    return "Round(04)";
                }
            } else if (_numberOfPages == 5) {
                if (position == 1) {
                    return "Round(08)";
                } else if (position == 2) {
                    return "Round(04)";
                }
            } else if (_numberOfPages == 4) {
                if (position == 1) {
                    return "Round(04)";
                }
            }

            return "";
        }
    }

    public static class PlaceholderFragment extends Fragment {

        AppDatabase db;

        static int _cupId;

        public PlaceholderFragment() {
            db = AppDatabase.getAppDatabase(getActivity());
        }

        public static PlaceholderFragment newInstance(int cupId) {

            PlaceholderFragment fragment = new PlaceholderFragment();

            _cupId = cupId;

            Bundle args = new Bundle();
            args.putInt("cupId", cupId);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Cup Players fragment
            View rootView = inflater.inflate(R.layout.fragment_cup_details_players, container, false);

            //int cupId = savedInstanceState.getInt("cupId", -1);

            List<Player> lstPlayers = db.cupPlayerDao().getPlayersByCupId(_cupId);

            CupPlayerAdapter adapter = new CupPlayerAdapter(getContext(), lstPlayers);

            ListView listView = rootView.findViewById(R.id.list_view_cup_players);
            listView.setAdapter(adapter);
            return rootView;
        }
    }
}
