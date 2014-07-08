package com.stuffbox.view;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;
import com.stuffbox.model.Category;
import com.stuffbox.model.DataSourceCategory;
import com.stuffbox.model.Icon;
import com.stuffbox.model.Item;
import com.stuffbox.view.helper.Utility;

public class SearchableActivity extends ActionBarActivity {
	private ListView itemListView;
	private ItemArrayAdapter itemAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		handleIntent(getIntent());

		// Anzeigen der Items:
		itemListView = (ListView) findViewById(R.id.listView);
		ArrayList<Item> allItems = new ArrayList<Item>();
		itemAdapter = new ItemArrayAdapter(this, allItems);
		itemListView.setAdapter(itemAdapter);

		// Groesse der Listen anhand der Anzahl der Eigenschaften neu setzen.
		Utility.setListViewHeightBasedOnChildren(itemListView, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Category currentCategory = Controller.getInstance()
				.getCurrentCategory();
		getSupportActionBar().setTitle(currentCategory.getName());

		Icon icon = currentCategory.getIcon();
		if (icon != null)
			getSupportActionBar().setIcon(icon.getDrawableId());

		if (currentCategory.getName().equals(DataSourceCategory.ROOT_CATEGORY)) {
			getMenuInflater().inflate(R.menu.list_categories_start, menu);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		} else {
			getMenuInflater().inflate(R.menu.list_categories, menu);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		getMenuInflater().inflate(R.menu.choose_items, menu);
		SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		searchView.setSearchableInfo(manager
				.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.menu_new_item:
			onNewItem();
			return true;
		case R.id.menu_new_categorie:
			onNewCategory(null);
			return true;
		case R.id.menu_edit_category:
			onEdit();
			return true;
		case R.id.menu_badge:
			Intent intent = new Intent();
			intent.setClassName(getPackageName(), BadgeActivity.class.getName());
			startActivity(intent);
			return true;
		case R.id.action_settings:
			return true;
			// Respond to the action bar's Up/Home button
		case android.R.id.home:
			if (!Controller.getInstance().getCurrentCategory().getName()
					.equals(DataSourceCategory.ROOT_CATEGORY)) {
				onBackPressed();
			}
			return true;
		case R.id.action_change_features:
			Intent intentChooseFeatures = new Intent();
			intentChooseFeatures.setClassName(getPackageName(),
					ListFeatureActivity.class.getName());
			startActivity(intentChooseFeatures);
			return true;
		case R.id.action_change_formulars:
			Intent intentChooseFormulars = new Intent();
			intentChooseFormulars.setClassName(getPackageName(),
					ListFormularActivity.class.getName());
			intentChooseFormulars.putExtra(
					ListFormularActivity.PURPOSE_IS_CHOOSING_FOR_UPDATE, true);
			startActivity(intentChooseFormulars);
			return true;
		case R.id.action_add_debug_entries:
			onInsertDebugEntries();
			return true;
		case R.id.action_search:
			// search action
			Intent intentSearchItem = new Intent();
			intentSearchItem.setClassName(getPackageName(),
					SearchableActivity.class.getName());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onSearchRequested() {
		Intent intentSearchItem = new Intent();
		intentSearchItem.setClassName(getPackageName(),
				SearchableActivity.class.getName());
		startActivity(intentSearchItem);
		return true;
	}

	public void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doSearch(query);
		}
	}

	private void doSearch(String query) {
		// Items holen
		ArrayList<Item> items = Controller.getInstance()
				.getItemsFromWordMatches(query, null);
		itemAdapter = new ItemArrayAdapter(this, items);
		itemListView.setAdapter(itemAdapter);

		// Groesse der Listen anhand der Anzahl der Eigenschaften neu setzen.
		Utility.setListViewHeightBasedOnChildren(itemListView, 0);
	}

	/**
	 * Erstellt eine neue Kategorie (zunächst Formular-Auswahl für das neue
	 * Item)
	 */
	public void onNewItem() {
		Intent intent = new Intent();
		intent.setClassName(getPackageName(),
				ListFormularActivity.class.getName());
		startActivity(intent);
	}

	public void onNewCategory(View view) {
		Intent intent = new Intent();
		intent.setClassName(getPackageName(),
				NewCategoryActivity.class.getName());
		startActivity(intent);
	}

	public void onInsertDebugEntries() {
		Controller.getInstance().insertDebugEntries(this);
		Category rootCategory = Controller.getInstance().getCurrentCategory();
		Controller.getInstance().setCurrentCategory(rootCategory);
		Intent intentToRoot = new Intent();
		intentToRoot.setClassName(getPackageName(),
				ListCategoriesActivity.class.getName());
		startActivity(intentToRoot);
		finish();
	}

	/**
	 * aendert die Kategorie
	 */
	public void onEdit() {
		Intent intent = new Intent();
		intent.putExtra(Controller.EXTRA_EDIT_CATEGORY, Controller
				.getInstance().getCurrentCategory());
		intent.setClassName(getPackageName(),
				NewCategoryActivity.class.getName());
		startActivity(intent);
	}
}