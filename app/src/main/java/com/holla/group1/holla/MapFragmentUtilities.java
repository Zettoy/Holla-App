package com.holla.group1.holla;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

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
