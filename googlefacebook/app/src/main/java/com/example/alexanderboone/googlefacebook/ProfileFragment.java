package com.example.alexanderboone.googlefacebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.googleplus.GooglePlusSocialNetwork;
import com.github.gorbin.asne.linkedin.LinkedInSocialNetwork;

import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements OnRequestSocialPersonCompleteListener {
    private String message = "Need simple social networks integration? Check this lbrary:";
    private String link = "https://github.com/gorbin/ASNE";

    private static final String NETWORK_ID = "NETWORK_ID";
    private SocialNetwork socialNetwork;
    private int networkId;
    private ImageView photo;
    private TextView name;
    private TextView id;
    private TextView info;

    private Button share;
    private RelativeLayout frame;

    public static ProfileFragment newInstannce(int id) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(NETWORK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        networkId = getArguments().containsKey(NETWORK_ID) ? getArguments().getInt(NETWORK_ID) : 0;
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        frame = (RelativeLayout) rootView.findViewById(R.id.frame);
        photo = (ImageView) rootView.findViewById(R.id.imageView);
        name = (TextView) rootView.findViewById(R.id.name);
        id = (TextView) rootView.findViewById(R.id.id);
        info = (TextView) rootView.findViewById(R.id.info);

        share = (Button) rootView.findViewById(R.id.share);
        share.setOnClickListener(shareClick);


        socialNetwork = MainFragment.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
        socialNetwork.requestCurrentPerson();

        MainActivity.showProgress("Loading social person");
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                socialNetwork.logout();
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson) {
        MainActivity.hideProgress();
        name.setText(socialPerson.name);
        id.setText(socialPerson.id);
        String socialPersonString = socialPerson.toString();
        String infoString = socialPersonString.substring(socialPersonString.indexOf("{")+1, socialPersonString.lastIndexOf("}"));
        info.setText(infoString.replace(", ", "\n"));
        Picasso.with(getActivity())
                .load(socialPerson.avatarURL)
                .into(photo);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        MainActivity.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }



    private View.OnClickListener shareClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

}