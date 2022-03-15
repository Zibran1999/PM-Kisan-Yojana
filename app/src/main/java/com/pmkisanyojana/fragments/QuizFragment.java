package com.pmkisanyojana.fragments;

import static com.pmkisanyojana.utils.CommonMethod.mInterstitialAd;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.ironsource.mediationsdk.IronSource;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.databinding.FragmentQuizBinding;
import com.pmkisanyojana.models.QuizModel;
import com.pmkisanyojana.utils.AdsViewModel;
import com.pmkisanyojana.utils.CommonMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class QuizFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    FragmentQuizBinding binding;
    PageViewModel pageViewModel;
    MaterialButton op1, op2, op3, op4;
    TextView question, questionNo;
    List<QuizModel> quizModelList = new ArrayList<>();
    Random random;
    int currentPos, questionAttemted = 1, currentScore = 0;


    ScrollView quiz;
    MaterialCardView score;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        question = binding.question;
        op1 = binding.optOneCard;
        op2 = binding.optTwoCard;
        op3 = binding.optThreeCard;
        op4 = binding.optFourCard;
        questionNo = binding.questionNo;
        quiz = binding.quiz;
        score = binding.score;
        CommonMethod.getBannerAds(requireActivity(), binding.adViewQuiz);
        AdsViewModel.destroyBanner();
        pageViewModel = new ViewModelProvider(requireActivity()).get(PageViewModel.class);
        pageViewModel.getquizQuestions().observe(requireActivity(), quizModelList1 -> {
            quizModelList.clear();
            if (!quizModelList1.getData().isEmpty()) {
                quiz.setVisibility(View.VISIBLE);
                quizModelList.addAll(quizModelList1.getData());
                random = new Random();
                currentPos = random.nextInt(quizModelList.size());
                setDataToViews(currentPos);
            } else {
                quiz.setVisibility(View.GONE);
            }
        });

        op1.setOnClickListener(this);
        op2.setOnClickListener(this);
        op3.setOnClickListener(this);
        op4.setOnClickListener(this);


        return root;
    }

    @SuppressLint("SetTextI18n")
    private void setDataToViews(int currentPos) {

        if (questionAttemted <= 15) {
            questionNo.setText(questionAttemted + "/15");
            question.setText(quizModelList.get(currentPos).getQues());
            op1.setText(quizModelList.get(currentPos).getOp1());
            op2.setText(quizModelList.get(currentPos).getOp2());
            op3.setText(quizModelList.get(currentPos).getOp3());
            op4.setText(quizModelList.get(currentPos).getOp4());

        } else {
            CommonMethod.interstitialAds(requireActivity());
            quiz.setVisibility(View.GONE);
            showScore();
        }

    }

    @SuppressLint("SetTextI18n")
    private void showScore() {
        score.setVisibility(View.VISIBLE);
        MaterialButton nextBtn;
        TextView scoreResult, cong;
        scoreResult = binding.scoreResult;
        nextBtn = binding.nextBtn;
        cong = binding.cong;
        if (currentScore < 7) {
            cong.setText("Try again!");
            nextBtn.setText("Play Again");
        } else if (currentScore > 7 && currentScore < 12) {
            cong.setText("Good!");

        } else if (currentScore > 12 && currentScore <= 15) {
            cong.setText("Great!");
        }
        scoreResult.setText(currentScore + "/15");
        nextBtn.setOnClickListener(v -> {
            quiz.setVisibility(View.VISIBLE);
            score.setVisibility(View.GONE);
            currentPos = random.nextInt(quizModelList.size());
            setDataToViews(currentPos);
            questionAttemted = 1;
            currentScore = 0;
        });


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.opt_one_card:
            case R.id.opt_two_card:
            case R.id.opt_three_card:
            case R.id.opt_four_card:

                MaterialButton button = (MaterialButton) v;
                checkAns(button);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void checkAns(MaterialButton button) {
        if (quizModelList.get(currentPos).getAns().trim().toLowerCase(Locale.ROOT).equals(button.getText().toString().trim().toLowerCase(Locale.ROOT))) {
            currentScore++;
            button.setBackgroundColor(Color.parseColor("#009637"));
            switch (button.getId()) {
                case R.id.opt_one_card:
                    binding.lottieWhatsapp.setAnimation(R.raw.right);
                    binding.lottieWhatsapp.playAnimation();
                    binding.lottieWhatsapp.setVisibility(View.VISIBLE);

                    break;
                case R.id.opt_two_card:
                    binding.lottieWhatsapp2.setAnimation(R.raw.right);
                    binding.lottieWhatsapp2.playAnimation();
                    binding.lottieWhatsapp2.setVisibility(View.VISIBLE);
                    break;
                case R.id.opt_three_card:
                    binding.lottieWhatsapp3.setAnimation(R.raw.right);
                    binding.lottieWhatsapp3.playAnimation();
                    binding.lottieWhatsapp3.setVisibility(View.VISIBLE);
                    break;
                case R.id.opt_four_card:
                    binding.lottieWhatsapp4.setAnimation(R.raw.right);
                    binding.lottieWhatsapp4.playAnimation();
                    binding.lottieWhatsapp4.setVisibility(View.VISIBLE);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + button.getId());
            }

            new Handler().postDelayed(() -> {
                currentPos = random.nextInt(quizModelList.size());
                resetButtonColor();
                setDataToViews(currentPos);
            }, 3000);
        } else {
            showAnswer();
            new Handler().postDelayed(() -> {
                currentPos = random.nextInt(quizModelList.size());
                resetButtonColor();
                setDataToViews(currentPos);
            }, 2000);
            button.setBackgroundColor(Color.parseColor("#ff0000"));
            switch (button.getId()) {
                case R.id.opt_one_card:
                    binding.lottieWhatsapp.setAnimation(R.raw.wrongcross);
                    binding.lottieWhatsapp.playAnimation();
                    binding.lottieWhatsapp.setVisibility(View.VISIBLE);

                    break;
                case R.id.opt_two_card:
                    binding.lottieWhatsapp2.setAnimation(R.raw.wrongcross);
                    binding.lottieWhatsapp2.playAnimation();
                    binding.lottieWhatsapp2.setVisibility(View.VISIBLE);
                    break;
                case R.id.opt_three_card:
                    binding.lottieWhatsapp3.setAnimation(R.raw.wrongcross);
                    binding.lottieWhatsapp3.playAnimation();
                    binding.lottieWhatsapp3.setVisibility(View.VISIBLE);
                    break;
                case R.id.opt_four_card:
                    binding.lottieWhatsapp4.setAnimation(R.raw.wrongcross);
                    binding.lottieWhatsapp4.playAnimation();
                    binding.lottieWhatsapp4.setVisibility(View.VISIBLE);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + button.getId());
            }

        }
        button.setTextColor(Color.parseColor("#ffffff"));
        questionAttemted++;
    }

    private void showAnswer() {
        op1.setClickable(false);
        op2.setClickable(false);
        op3.setClickable(false);
        op4.setClickable(false);
        if (quizModelList.get(currentPos).getAns().trim().toLowerCase(Locale.ROOT).equals(op1.getText().toString().trim().toLowerCase(Locale.ROOT))) {

            op1.setBackgroundColor(Color.parseColor("#009637"));
            op1.setTextColor(Color.parseColor("#ffffff"));

        } else if (quizModelList.get(currentPos).getAns().trim().toLowerCase(Locale.ROOT).equals(op2.getText().toString().trim().toLowerCase(Locale.ROOT))) {

            op2.setBackgroundColor(Color.parseColor("#009637"));
            op2.setTextColor(Color.parseColor("#ffffff"));


        } else if (quizModelList.get(currentPos).getAns().trim().toLowerCase(Locale.ROOT).equals(op3.getText().toString().trim().toLowerCase(Locale.ROOT))) {

            op3.setBackgroundColor(Color.parseColor("#009637"));
            op3.setTextColor(Color.parseColor("#ffffff"));


        } else if (quizModelList.get(currentPos).getAns().trim().toLowerCase(Locale.ROOT).equals(op4.getText().toString().trim().toLowerCase(Locale.ROOT))) {
            op4.setBackgroundColor(Color.parseColor("#009637"));
            op4.setTextColor(Color.parseColor("#ffffff"));

        }

    }

    private void resetButtonColor() {
        CommonMethod.getBannerAds(requireActivity(), binding.adViewQuiz);

        op1.setClickable(true);
        op2.setClickable(true);
        op3.setClickable(true);
        op4.setClickable(true);

        op1.setBackgroundColor(Color.parseColor("#ffffff"));
        op2.setBackgroundColor(Color.parseColor("#ffffff"));
        op3.setBackgroundColor(Color.parseColor("#ffffff"));
        op4.setBackgroundColor(Color.parseColor("#ffffff"));
        op1.setTextColor(Color.parseColor("#000000"));
        op2.setTextColor(Color.parseColor("#000000"));
        op3.setTextColor(Color.parseColor("#000000"));
        op4.setTextColor(Color.parseColor("#000000"));
        binding.lottieWhatsapp.setVisibility(View.GONE);
        binding.lottieWhatsapp2.setVisibility(View.GONE);
        binding.lottieWhatsapp3.setVisibility(View.GONE);
        binding.lottieWhatsapp4.setVisibility(View.GONE);


    }
    @Override
    public void onPause() {
        super.onPause();
        IronSource.onPause(requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(requireActivity());
    }

}