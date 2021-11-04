package Tabs;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import Fragments.Community_All_Fragment;
import Fragments.Community_Mine_Fragment;


/**
 * Created by Abhi on 9/7/2018.
 */

public class Pager_Community extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager_Community(FragmentManager fm, int tabCount) {
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



                //Community_All_Fragment tab1 = new Community_All_Fragment();
                Community_All_Fragment tab1 = new Community_All_Fragment();
                return tab1;
            case 1:

                Community_Mine_Fragment tab2 = new Community_Mine_Fragment();
                return tab2;

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