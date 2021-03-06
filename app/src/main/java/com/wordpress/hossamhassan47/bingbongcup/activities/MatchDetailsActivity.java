package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.Helper.RoundImage;
import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupRound;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGame;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGameDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;

import java.util.List;

public class MatchDetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private int currentSelectedPage = 0;
    int roundMatchId;
    int winnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        roundMatchId = getIntent().getIntExtra("roundMatchId", -1);
        winnerId = getIntent().getIntExtra("winnerId", -1);

        SetViewPager();
    }

    public void SetViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this, roundMatchId, winnerId);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentSelectedPage);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentSelectedPage = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<MatchGame> _matchGameList;
        private final int _numberOfPages;
        private final int _roundMatchId;
        private final int _winnerId;

        public SectionsPagerAdapter(FragmentManager fm, Context c, int roundMatchId, int winnerId) {
            super(fm);

            AppDatabase db = AppDatabase.getAppDatabase(c);
            _matchGameList = db.matchGameDao().loadAllMatchGames(roundMatchId);

            _numberOfPages = _matchGameList.size();
            _roundMatchId = roundMatchId;
            _winnerId = winnerId;
        }

        @Override
        public Fragment getItem(int position) {
            int matchGameId = _matchGameList.get(position).matchGameId;

            return PlaceholderFragment.newInstance(matchGameId, _winnerId);
        }

        @Override
        public int getCount() {
            return _numberOfPages;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Game#" + _matchGameList.get(position).gameNo;
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_MATCH_GAME_ID = "match_game_Id";
        private static final String ARG_MATCH_WINNER_ID = "match_winner_Id";

        private int matchGameId = 0;
        private int roundMatchId = 0;
        private int player1Id = 0;
        private int player2Id = 0;
        private int maximumScore = 21;
        private int player1Score = 0;
        private int player2Score = 0;

        TextView txtPlayer1Score;
        TextView txtPlayer2Score;

        RoundImage roundedImage1;
        RoundImage roundedImage2;

        AppDatabase db;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int matchGameId, int winnerId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MATCH_GAME_ID, matchGameId);
            args.putInt(ARG_MATCH_WINNER_ID, winnerId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_match_details, container, false);

            matchGameId = getArguments().getInt(ARG_MATCH_GAME_ID);
            int winnerId = getArguments().getInt(ARG_MATCH_WINNER_ID);

            db = AppDatabase.getAppDatabase(getActivity());
            MatchGameDetails matchGameDetails = db.matchGameDao().loadMatchGameDetailsById(matchGameId);

            roundMatchId = matchGameDetails.matchGame.fk_roundMatchId;

            player1Id = matchGameDetails.matchPlayer1Id;
            player2Id = matchGameDetails.matchPlayer2Id;

            // Players Name
            TextView txtPlayer1Name = rootView.findViewById(R.id.text_view_player_1_name);
            txtPlayer1Name.setText(matchGameDetails.player1Name);

            TextView txtPlayer2Name = rootView.findViewById(R.id.text_view_player_2_name);
            txtPlayer2Name.setText(matchGameDetails.player2Name);

            ImageView imgPlayer1Image = rootView.findViewById(R.id.image_view_player_1_image);
            if (matchGameDetails.player1ImageSrc != null) {
                Bitmap bm = BitmapFactory.decodeFile(matchGameDetails.player1ImageSrc);
                roundedImage1 = new RoundImage(bm);
                imgPlayer1Image.setImageDrawable(roundedImage1);
            }else {
                imgPlayer1Image.setImageResource(R.drawable.ic_face_white_48dp);
            }

            ImageView imgPlayer2Image = rootView.findViewById(R.id.image_view_player_2_image);
            if (matchGameDetails.player2ImageSrc != null) {
                Bitmap bm = BitmapFactory.decodeFile(matchGameDetails.player2ImageSrc);
                roundedImage2 = new RoundImage(bm);
                imgPlayer2Image.setImageDrawable(roundedImage2);
            }else {
                imgPlayer2Image.setImageResource(R.drawable.ic_face_white_48dp);
            }

            // Winning Game Count
            List<MatchGameDetails> lstMatchGameDetails = db.matchGameDao().loadMatchGameDetailsByRoundMatchId(roundMatchId);

            LinearLayout linearLayoutPlayer1WinningGameCount = rootView.findViewById(R.id.linear_layout_player_1_winning_game_count);
            LinearLayout linearLayoutPlayer2WinningGameCount = rootView.findViewById(R.id.linear_layout_player_2_winning_game_count);

            for (int i = 0; i < lstMatchGameDetails.size(); i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.drawable.ic_star_border_white_24dp);

                if (lstMatchGameDetails.get(i).matchGame.winnerId == player1Id) {
                    linearLayoutPlayer1WinningGameCount.addView(imageView);
                } else if (lstMatchGameDetails.get(i).matchGame.winnerId == player2Id) {
                    linearLayoutPlayer2WinningGameCount.addView(imageView);
                }
            }

            // Score
            txtPlayer1Score = rootView.findViewById(R.id.text_view_player_1_score);
            player1Score = matchGameDetails.matchGame.player1Score;
            txtPlayer1Score.setText(player1Score + "");
            if (winnerId <= 0) {
                txtPlayer1Score.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (player1Score < maximumScore) {
                            player1Score += 1;
                        }

                        txtPlayer1Score.setText(player1Score + "");
                    }
                });
            }

            txtPlayer2Score = rootView.findViewById(R.id.text_view_player_2_score);
            player2Score = matchGameDetails.matchGame.player2Score;
            txtPlayer2Score.setText(player2Score + "");
            if (winnerId <= 0) {
                txtPlayer2Score.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (player2Score < maximumScore) {
                            player2Score += 1;
                        }

                        txtPlayer2Score.setText(player2Score + "");
                    }
                });
            }

            // Negative
            LinearLayout player1Neg = rootView.findViewById(R.id.linear_layout_player_1_neg);
            if (winnerId <= 0) {
                player1Neg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (player1Score > 0) {
                            player1Score -= 1;
                        }

                        txtPlayer1Score.setText(player1Score + "");
                    }
                });
            }

            LinearLayout player2Neg = rootView.findViewById(R.id.linear_layout_player_2_neg);
            if (winnerId <= 0) {
                player2Neg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (player2Score > 0) {
                            player2Score -= 1;
                        }

                        txtPlayer2Score.setText(player2Score + "");
                    }
                });
            }

            // Save
            LinearLayout saveResult = rootView.findViewById(R.id.linear_layout_save_result);
            if (winnerId <= 0) {
                saveResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        MatchGame matchGame = db.matchGameDao().loadMatchGameById(matchGameId);
                        matchGame.player1Id = player1Id;
                        matchGame.player2Id = player2Id;
                        matchGame.player1Score = player1Score;
                        matchGame.player2Score = player2Score;

                        if (player1Score > player2Score) {
                            matchGame.winnerId = player1Id;
                        } else if (player2Score > player1Score) {
                            matchGame.winnerId = player2Id;
                        } else {
                            matchGame.winnerId = 0;
                        }

                        int count = db.matchGameDao().updateMatchGame(matchGame);
                        if (count > 0) {
                            ((MatchDetailsActivity) getActivity()).SetViewPager();
                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

            if (winnerId > 0) {
                saveResult.setVisibility(View.INVISIBLE);
                player1Neg.setVisibility(View.INVISIBLE);
                player2Neg.setVisibility(View.INVISIBLE);
            }

            return rootView;
        }
    }

}
