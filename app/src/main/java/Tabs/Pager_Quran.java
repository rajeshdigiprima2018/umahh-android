package Tabs;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import Fragments.Juz_outer_Fragment;
import Fragments.Sura_Fragment;


/**
 * Created by Abhi on 9/7/2018.
 */

public class Pager_Quran extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager_Quran

    (FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:



                Sura_Fragment tab1 = new Sura_Fragment();
                return tab1;

            case 1:

                Juz_outer_Fragment tab2 = new Juz_outer_Fragment();
                return tab2;
/*
            case 2:

                My_quran_Fragment tab3 = new My_quran_Fragment();
                return tab3;*/

            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}