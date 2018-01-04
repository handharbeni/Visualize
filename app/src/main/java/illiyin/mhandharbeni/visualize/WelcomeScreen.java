package illiyin.mhandharbeni.visualize;

import android.support.v4.app.Fragment;

import com.stephentuso.welcome.FragmentWelcomePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

import illiyin.mhandharbeni.visualize.slide.FragmentSlideOne;
import illiyin.mhandharbeni.visualize.slide.FragmentSlideThree;
import illiyin.mhandharbeni.visualize.slide.FragmentSlideTwo;

/**
 * Created by root on 12/21/17.
 */

public class WelcomeScreen extends WelcomeActivity {
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.slide1)
//                .page(new TitlePage(R.drawable.slide_1, "").background(R.color.slide1))
//                .page(new BasicPage(R.drawable.slide_2, "", "").background(R.color.slide2))
//                .page(new BasicPage(R.drawable.slide_3,"", "").background(R.color.slide3))
                .page(new FragmentWelcomePage() {
                    @Override
                    protected Fragment fragment() {
                        return new FragmentSlideOne();
                    }
                }.background(R.color.slide1))
                .page(new FragmentWelcomePage() {
                    @Override
                    protected Fragment fragment() {
                        return new FragmentSlideTwo();
                    }
                }.background(R.color.slide2))
                .page(new FragmentWelcomePage() {
                    @Override
                    protected Fragment fragment() {
                        return new FragmentSlideThree();
                    }
                }.background(R.color.slide1))
                .swipeToDismiss(true)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }
    public static String welcomeKey() {
        return "WelcomeScreen";
    }
}
