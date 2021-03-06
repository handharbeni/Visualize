package illiyin.mhandharbeni.visualize.slide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stephentuso.welcome.WelcomePage;
import com.stephentuso.welcome.WelcomeUtils;

import illiyin.mhandharbeni.visualize.R;

/**
 * Created by root on 1/2/18.
 */

public class FragmentSlideTwo extends Fragment implements WelcomePage.OnChangeListener {

    private ViewGroup rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.__slide_fragment_slide_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            rootLayout = view.findViewById(R.id.layout);

//            view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getContext(), "Button clicked in custom fragment", Toast.LENGTH_SHORT).show();
//                }
//            });
    }

    @Override
    public void onWelcomeScreenPageScrolled(int pageIndex, float offset, int offsetPixels) {
        if (rootLayout != null)
            WelcomeUtils.applyParallaxEffect(rootLayout, true, offsetPixels, 0.3f, 0.2f);
    }

    @Override
    public void onWelcomeScreenPageSelected(int pageIndex, int selectedPageIndex) {
            //Not used
    }

    @Override
    public void onWelcomeScreenPageScrollStateChanged(int pageIndex, int state) {
        //Not used
    }
}
