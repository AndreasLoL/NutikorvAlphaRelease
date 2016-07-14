package com.nutikorv.andreas.nutikorvalpha;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * Created by ANDREAS on 12.07.2016.
 */
public class InnerMainFragment extends Fragment {

    ViewPager viewPager = HomeFragment.viewPager;
    Activity mActivity;
    ExpandableListAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products_inner, container, false);

        ExpandableListView choices = (ExpandableListView) rootView.findViewById(R.id.listView);

        ReadProducts r = new ReadProducts(mActivity, "products.txt");

        adapter = new ExpandableAdapter(mActivity, r.getGroupList(), r.getFoodCollection());


        choices.setAdapter(adapter);

        if (GlobalParameters.b == null) {
            GlobalParameters.b = new Basket("My temporary basket!");
        }

        choices.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                createProductDialog((Product) adapter.getChild(groupPosition, childPosition), getContext());


//                Product temp = (Product) adapter.getChild(groupPosition, childPosition);
//
//                GlobalParameters.b.addToBasket(temp);
//                Toast.makeText(getContext(), "Added to basket!", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        Button basketButton = (Button) rootView.findViewById(R.id.basketButton);

        basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);

            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void createProductDialog(Product p, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_product);
        dialog.setTitle(p.getName());

        final Product p2 = p;

        TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
        title.setText(p.getName());


        TextView text = (TextView) dialog.findViewById(R.id.dialogDescription);
        text.setText(p.getProductDescription());
        ImageView image = (ImageView) dialog.findViewById(R.id.dialogImage);
        UrlImageViewHelper.setUrlDrawable(image, p.getImgURL());

        TextView selverPrice = (TextView) dialog.findViewById(R.id.selverPrice);
        selverPrice.setText("Selveri hind: " + String.format("%.2f", p.getSelverPrice()) + "€");

        TextView prismaPrice = (TextView) dialog.findViewById(R.id.prismaPrice);
        prismaPrice.setText("Prisma hind: " + String.format("%.2f", p.getPrismaPrice()) + "€");

        TextView maximaPrice = (TextView) dialog.findViewById(R.id.maximaPrice);
        maximaPrice.setText("Maxima hind: " + String.format("%.2f", p.getMaximaPrice()) + "€");


        Button dialogButton = (Button) dialog.findViewById(R.id.addProduct);



        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalParameters.b.addToBasket(p2);
                Toast.makeText(getContext(), "Added to basket!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
