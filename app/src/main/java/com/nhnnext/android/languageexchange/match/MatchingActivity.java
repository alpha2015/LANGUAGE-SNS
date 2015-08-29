package com.nhnnext.android.languageexchange.match;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.nhnnext.android.languageexchange.Model.MessageInfo;
import com.nhnnext.android.languageexchange.Model.User;
import com.nhnnext.android.languageexchange.Model.UserParcelable;
import com.nhnnext.android.languageexchange.R;
import com.nhnnext.android.languageexchange.common.FriendListAdapter;
import com.nhnnext.android.languageexchange.common.GsonRequest;
import com.nhnnext.android.languageexchange.common.ImageLoadHelper;
import com.nhnnext.android.languageexchange.common.NotiItemAdapter;
import com.nhnnext.android.languageexchange.common.UrlFactory;
import com.nhnnext.android.languageexchange.user.Fragment_UserProfileDialog;
import com.parse.ParseInstallation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alpha on 2015. 7. 22..
 * Class MatchingActivity : app 핵심 activity
 * fragemnt : 타임라인, 매칭시작, 정보수정
 * action bar : 사용자 검색, push notification list
 */
public class MatchingActivity extends AppCompatActivity {
    private ArrayList<MessageInfo> messageList;
    private ListView mRightDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView friendListView;
    private FriendListAdapter friendListAdapter;
    private RequestQueue queue;
    private GsonRequest<ArrayList<User>> searchRequest;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private MatchScreenSlidePagerAdapter mPagerAdapter;
    private UserParcelable user;
    private TabLayout tabLayout;
    private Menu menu;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        mContext = this;

        queue = Volley.newRequestQueue(this);
        friendListView = (ListView) findViewById(R.id.list_view1);

        messageList = new ArrayList<>(); //메시지리스트 저장을 위한 ArrayList
        messageList.add(new MessageInfo(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_1), "name1", "hello? my name is name1", new Date()));
        messageList.add(new MessageInfo(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_2), "name2", "hello? my name is name2", new Date()));
        messageList.add(new MessageInfo(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_3), "name3", "hello? my name is name3", new Date()));
        messageList.add(new MessageInfo(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_4), "name4", "hello? my name is name4", new Date()));
        messageList.add(new MessageInfo(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_5), "name5", "hello? my name is name5", new Date()));
        messageList.add(new MessageInfo(BitmapFactory.decodeResource(getResources(), R.drawable.sample_image_6), "name6", "hello? my name is name6", new Date()));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightDrawerList = (ListView) findViewById(R.id.right_drawer);

        // Set the adapter for the list view
        mRightDrawerList.setAdapter(new NotiItemAdapter(this,
                R.layout.notification_list_item, messageList));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        // Instantiate a ViewPager and a PagerAdapter.
        // Fragment 초기화
        user = getIntent().getExtras().getParcelable("user");


        // push notification email 등록 installation
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("email", user.getEmail());
        installation.saveInBackground();


        mPager = (ViewPager) findViewById(R.id.pager);

        mPagerAdapter = new MatchScreenSlidePagerAdapter(getSupportFragmentManager());
        //언어 선택 Fragment
        mPagerAdapter.addFragment(new Fragment_TimeLine());
        //매칭 시작 Fragment
        mPagerAdapter.addFragment(Fragment_StartMatch.newInstance(user));
        //개인정보 수정 Fragment
        mPagerAdapter.addFragment(Fragment_UpdateUserInfo.newInstance(user));
        mPager.setAdapter(mPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(mPager);

        LinearLayout tab1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((ImageView) tab1.findViewById(R.id.tab_icon)).setImageResource(R.drawable.ic_contacts_black_24dp);
        ((TextView) tab1.findViewById(R.id.tab_text)).setText("TimeLine");
        tabLayout.getTabAt(0).setCustomView(tab1);
        LinearLayout tab2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((ImageView) tab2.findViewById(R.id.tab_icon)).setImageResource(R.drawable.ic_search_black_24dp);
        ((TextView) tab2.findViewById(R.id.tab_text)).setText("Match");
        tabLayout.getTabAt(1).setCustomView(tab2);
        LinearLayout tab3 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ((ImageView) tab3.findViewById(R.id.tab_icon)).setImageResource(R.drawable.ic_settings_black_24dp);
        ((TextView) tab3.findViewById(R.id.tab_text)).setText("MyInfo");
        tabLayout.getTabAt(2).setCustomView(tab3);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4EC89C")));

    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mRightDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private Menu getMenu() {
        return menu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.matching_activity_actions, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search user");
        searchView.setOnQueryTextListener(queryTextListener);
        final MenuItem friendListItem = menu.findItem(R.id.action_friends_list);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);


        // When using the support library, the setOnActionExpandListener() method is
        // static and accepts the MenuItem object as an argument
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                getMenu().findItem(R.id.action_friends_list).setVisible(true);
                friendListView.setAdapter(null);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                getMenu().findItem(R.id.action_friends_list).setVisible(false);
                return true;  // Return true to expand action view
            }
        });

        friendListItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean rightDrawerOpen = mDrawerLayout.isDrawerOpen(mRightDrawerList);
                if (!rightDrawerOpen) {
                    mDrawerLayout.openDrawer(mRightDrawerList);
                } else {
                    mDrawerLayout.closeDrawer(mRightDrawerList);

                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class MatchScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public MatchScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    /**
     * Class DrawerItemClickListener : push notification list item 선택 이벤트 listener
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO 알림 item 클릭시 내용 팝업
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        /**
         * Method onQueryTextSubmit(final String query)
         * 검색시 server db에 해당 query 포함된 사용자이름이나 이메일 검색후 user list 표시
         * @param query 입력된 검색 query
         * @return
         */
        @SuppressWarnings("unchecked")
        @Override
        public boolean onQueryTextSubmit(final String query) {
            Type collectionType = new TypeToken<ArrayList<User>>() {
            }.getType();
            searchRequest = new GsonRequest<ArrayList<User>>(UrlFactory.SEARCH_USERS, collectionType, null,
                    new Response.Listener<ArrayList<User>>() {
                        @Override
                        public void onResponse(ArrayList<User> users) {
                            friendListAdapter = new FriendListAdapter(MatchingActivity.this, users, ImageLoadHelper.getInstance(mContext).getImageLoader());
                            friendListView.setAdapter(friendListAdapter);
                            friendListView.setTextFilterEnabled(false);
                            friendListView.bringToFront();

                            friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View v,
                                                        int position, long id) {
                                    UserParcelable parcelUser = new UserParcelable((User) friendListView.getAdapter().getItem(position));
                                    Fragment_UserProfileDialog.newInstance(parcelUser).show(getFragmentManager(), "dialog");
                                }
                            });

                            Toast.makeText(getApplicationContext(), "검색 성공", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), "검색 실패", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("query", query);
                    return params;
                }
            };
            queue.add(searchRequest);
            return false;
        }

        /**
         * Method onQueryTextChange(String newText)
         * 공백시 검색 결과 list item 제거
         * @param newText 입력중인 query
         * @return
         */
        @Override
        public boolean onQueryTextChange(String newText) {
            if (newText.equals(""))
                friendListView.setAdapter(null);
            return false;
        }
    };
}
