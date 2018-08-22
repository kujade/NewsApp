package com.example.android.newsappdenisa;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsappdenisa.R.string.no_articles;
import static com.example.android.newsappdenisa.R.string.no_internet;

public class Guardian extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Post>> {

    private static final int POST_LOADER_ID = 1;

    //Adapter for a listview
    private GuardianAdapter mAdapter;

    //URL which leads to Guardian data
    private static final String POST_REQUEST_URL ="http://content.guardianapis.com/search?";

    //TextView for empty list
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_guardian );

        ListView PostListView = (ListView) findViewById( R.id.list );

        //Creating a new adapter
        mAdapter = new GuardianAdapter( this, new ArrayList<Post>() );
        PostListView.setAdapter( mAdapter );

        mEmptyTextView = findViewById( R.id.empty_view );
        PostListView.setEmptyView( mEmptyTextView );

        //When view is clicked, there is a click listener for transfering to Guardian website
        PostListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Post currentPost = mAdapter.getItem( position );
                Uri postUri = Uri.parse( currentPost.getUrl() );
                Intent websiteIntent = new Intent( Intent.ACTION_VIEW, postUri );
                startActivity( websiteIntent );
            }
        } );

        // Check network connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService( Context.CONNECTIVITY_SERVICE );

        // Check info about network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        // If network connection is found, send data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader( POST_LOADER_ID, null, this );
        } else {
            //Display error message when there is no connection.
            View loadingIndicator = findViewById( R.id.loading_indicator );
            loadingIndicator.setVisibility( View.GONE );
            mEmptyTextView.setText( no_internet );
        }
    }

    @Override    // New loader is created
    public Loader<List<Post>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String section = sharedPreferences.getString(getString(R.string.section_key), getString(R.string.section_default));
        String order = sharedPreferences.getString(getString(R.string.order_key), getString(R.string.order_default));

        Uri baseUri = Uri.parse(POST_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", order);
        if (!section.equals(getString(R.string.section_default))) {
            uriBuilder.appendQueryParameter("section", section);
        }
        return new PostLoader(this, uriBuilder.toString());
    }


    @Override
    //Does not show loading sign, shows if no posts are found
    public void onLoadFinished(Loader<List<Post>> loader, List<Post> allPost) {
        View loadingIndicator = findViewById( R.id.loading_indicator );
        loadingIndicator.setVisibility( View.GONE );
        mEmptyTextView.setText( no_articles );
        mAdapter.clear();
        //Shows valid list of posts
        if (allPost != null && !allPost.isEmpty()) {
            mAdapter.addAll( allPost );
        }
    }

    @Override
    //Clear the loader to reset data
    public void onLoaderReset(Loader<List<Post>> loader) {
        mAdapter.clear();
    }

    //Initialize content of menu
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    //Called when item in menu is selected
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if ( id == R.id.guardian_settings){
            Intent settingsIntent = new Intent (this, GuardianSettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}

