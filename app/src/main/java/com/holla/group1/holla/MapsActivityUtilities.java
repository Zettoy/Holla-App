package com.holla.group1.holla;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MapsActivityUtilities {
    private static Fragment getOverlayFragment(FragmentActivity activity) {
        Fragment overlayFragment = activity.getSupportFragmentManager().findFragmentById(R.id.post_map_overlay_frag);
        return overlayFragment;

    }

    public static void showOverlay(FragmentActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.show(getOverlayFragment(activity));
        ft.commit();
    }

    public static void hideOverlay(FragmentActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.hide(getOverlayFragment(activity));
        ft.commit();
    }

    public static void setOverlayPost(Post post, FragmentActivity fragmentActivity) {
        PostMapOverlay overlayFragment = (PostMapOverlay) getOverlayFragment(fragmentActivity);
        overlayFragment.showPost(post);
    }
}
