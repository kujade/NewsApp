package com.example.android.newsappdenisa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class GuardianAdapter extends ArrayAdapter<Post> {

    private List<Post> postList;
    public GuardianAdapter(Context context, List<Post> post) {
        super( context, 0, post );
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from( getContext() ).inflate(
                    R.layout.guardian_list_item, parent, false );
        }
        //Get the post on the position in the list
        Post currentPost = getItem( position );

        // Find the TextView in the guardian_list_item.xml layout with the ID section
        TextView sectionView =  listItemView.findViewById( R.id.section );
        sectionView.setText( currentPost.getSection() );
        // Find the TextView in the guardian_list_item.xm layout with the ID name_of_the_titleOfPost
        TextView titleTextView =  listItemView.findViewById( R.id.title );
        titleTextView.setText( currentPost.getTitle() );
        // Find the TextView in the guardian_list_item.xm layout with the ID date
        TextView dateView =  listItemView.findViewById( R.id.date );
        dateView.setText( currentPost.getDate() );
        // Find the TextView in the guardian_list_item.xm layout with the ID author
        TextView authorView = listItemView.findViewById( R.id.author );
        authorView.setText( currentPost.getAuthor() );

        return listItemView;
    }
}
