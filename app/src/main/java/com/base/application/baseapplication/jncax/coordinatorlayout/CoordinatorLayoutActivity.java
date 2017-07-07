package com.base.application.baseapplication.jncax.coordinatorlayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.base.application.baseapplication.R;
import com.base.application.baseapplication.jncax.smarttab.SmartTabItemView;
import com.base.application.baseapplication.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adminchen on 16/9/5 17:40.
 */
public class CoordinatorLayoutActivity extends AppCompatActivity
{
	private TabLayout tabLayout;

	private Toolbar toolbar;

	/**
	 * 不能用listview，因为listview 没有实现NestedScrollingChild这个接口
	 **/
	private RecyclerView recyclerView;
	//	private RecyclerView recyclerView1;

	private FloatingActionButton floatingActionButton;

	private ImageView imageView;

	private ViewPager viewPager;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.acjn_activity_coordinationlayout);

		initActivity();
		initData();
	}

	private void initActivity()
	{
		tabLayout = (TabLayout)findViewById(R.id.tabs);
		toolbar = (Toolbar)findViewById(R.id.toolbar);
//		recyclerView = (RecyclerView)findViewById(R.id.recyclerview_content);
		floatingActionButton = (FloatingActionButton)findViewById(R.id.action_button_tab);
		viewPager = (ViewPager) findViewById(R.id.view_pager_content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.toolbar_layout,menu);
		return true;
	}

	private void initData()
	{
		/**setLogo,setSubtitle等不能和CollapsingToolbarLayout混合使用**/
		toolbar.setLogo(R.drawable.acjn_service_medical_icon);
//		toolbar.setTitle("THIS IS NEW TITLE");
//		toolbar.setTitleTextColor(R.color.pub_color_two);
		toolbar.setSubtitle("this is subtitle");
		toolbar.inflateMenu(R.menu.toolbar_layout);

		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.acjn_service_live_icon);

		/**该方法要写在setSupportActionBar方法之后才会生效**/
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Toast.makeText(CoordinatorLayoutActivity.this,"NavigationIcon clicked",
						Toast.LENGTH_SHORT)
					 .show();
			}
		});

		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				int menuItemId = item.getItemId();
				if(menuItemId == R.id.action_search)
				{
					Toast.makeText(CoordinatorLayoutActivity.this,"menu_search",Toast.LENGTH_SHORT)
						 .show();

				}
				else if(menuItemId == R.id.action_notification)
				{
					Toast.makeText(CoordinatorLayoutActivity.this,"menu_notifications",
							Toast.LENGTH_SHORT).show();

				}
				else if(menuItemId == R.id.action_item1)
				{
					Toast.makeText(CoordinatorLayoutActivity.this,"item_01",Toast.LENGTH_SHORT)
						 .show();

				}
				else if(menuItemId == R.id.action_item2)
				{
					Toast.makeText(CoordinatorLayoutActivity.this,"item_02",Toast.LENGTH_SHORT)
						 .show();

				}
				return true;
			}
		});
		//		recyclerView1.setLayoutManager(new LinearLayoutManager(this));
		//		recyclerView1.setAdapter(new RecyclerViewAdapter(this,getListData()));

//		tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
		tabLayout.addTab(tabLayout.newTab().setText("one"));
		tabLayout.addTab(tabLayout.newTab().setText("two"));
		tabLayout.addTab(tabLayout.newTab().setText("three"));
		tabLayout.addTab(tabLayout.newTab().setText("four"));

		List<Fragment> fragments = new ArrayList<>();

		fragments.add(new RecyclerViewFragment());
		fragments.add(new RecyclerViewFragment());
		fragments.add(new RecyclerViewFragment());
		fragments.add(new RecyclerViewFragment());
		MyViewPagerAdapter pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
		pagerAdapter.setFragmentList(fragments);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
		tabLayout.setupWithViewPager(viewPager);

		tabLayout.getTabAt(0).setCustomView(new SmartTabItemView(this,null));







		floatingActionButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

				Snackbar.make(view,"FAB",Snackbar.LENGTH_LONG)
						.setAction("cancel",new View.OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								//这里的单击事件代表点击消除Action后的响应事件
							}
						})
						.show();
			}
		});

//		recyclerView.setLayoutManager(new LinearLayoutManager(this));
//		recyclerView.setAdapter(new RecyclerViewAdapter(this,getListData()));
	}

	private List<String> getListData()
	{
		List<String> list = new ArrayList<>();
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		list.add("2343543453");
		return list;
	}
}
