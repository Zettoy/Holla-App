package com.holla.group1.holla.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.holla.group1.holla.PostMapOverlay;
import com.holla.group1.holla.R;
import com.holla.group1.holla.post.Post;

public class MapFragmentUtilities {
    private static Fragment getOverlayFragment(Fragment fragment) {
        return fragment.getChildFragmentManager().findFragmentById(R.id.post_map_overlay_frag);

    }

    public static void showOverlay(Fragment fragment) {
        FragmentTransaction ft = fragment.getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.show(getOverlayFragment(fragment));
        ft.commit();
    }

    public static void hideOverlay(Fragment fragment) {
        FragmentTransaction ft = fragment.getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.hide(getOverlayFragment(fragment));
        ft.commit();
    }

    public static void setOverlayPost(Post post, Fragment fragment) {
        PostMapOverlay overlayFragment = (PostMapOverlay) getOverlayFragment(fragment);
        overlayFragment.showPost(post);
    }
}
