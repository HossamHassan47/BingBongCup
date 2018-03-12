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

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.adapters.CupPlayerAdapter;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;

import java.util.List;

public class CupDetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cup_details);

        int numberOfPages = getIntent().getIntExtra("numberOfPages", 2);

        int cupId = getIntent().getIntExtra("cupId", -1);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), numberOfPages, cupId);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pagerCupDetails);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    // Pages Adapter
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final int _numberOfPages;
        private final int _cupId;

        public SectionsPagerAdapter(FragmentManager fm, int numberOfPages, int cupId) {

            super(fm);
            _numberOfPages = numberOfPages;
            _cupId = cupId;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // Players
                    return CupPlayersFragment.newInstance(position + 1, _cupId);
                case 1:
                    // Players
                    //return CupPlayersFragment.newInstance(position + 1, _cupId);
                case 2:
                    //rootView = inflater.inflate(R.layout.fragment_obj_list, container, false);
                    break;
                case 3:
                    //rootView = inflater.inflate(R.layout.fragment_obj_list, container, false);
                    break;
                case 4:
                    //rootView = inflater.inflate(R.layout.fragment_about, container, false);
                    break;
                default:
                    //rootView = inflater.inflate(R.layout.fragment_obj_list, container, false);
            }

            return new Fragment();
        }

        @Override
        public int getCount() {
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

    // Page Fragment
    public static class CupPlayersFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER";
        private static final String ARG_CUP_ID = "ARG_CUP_ID";

        public CupPlayersFragment() {
        }

        public static CupPlayersFragment newInstance(int sectionNumber, int cupId) {
            CupPlayersFragment fragment = new CupPlayersFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_CUP_ID, cupId);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cup_details_players, container, false);

            int cupId = getArguments().getInt(ARG_CUP_ID);

            AppDatabase db = AppDatabase.getAppDatabase(getActivity());

            List<Player> lstPlayers = db.cupPlayerDao().getPlayersByCupId(cupId);

            CupPlayerAdapter adapter = new CupPlayerAdapter(getContext(), lstPlayers);

            ListView listView = rootView.findViewById(R.id.list_view_cup_players);
            listView.setAdapter(adapter);

            return rootView;
        }
    }
}
