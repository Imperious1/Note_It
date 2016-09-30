package imperiumnet.imperious.noteit.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shadow on 6/30/2016.
 */
public abstract class CustomArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<String> folderNames;

    public CustomArrayAdapter(Context context, ArrayList<String> data) {
        super(context, 0, data);
        this.folderNames = data;
    }

    public abstract void onClick2(View v, int position);

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        convertView.setBackgroundResource(outValue.resourceId);
        convertView.setClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick2(v, position);
            }
        });
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(folderNames.get(position));
        textView.setAnimation(AnimationUtils.loadAnimation(parent.getContext(), android.R.anim.fade_in));
        return convertView;
    }
}