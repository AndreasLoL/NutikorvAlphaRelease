package com.nutikorv.andreas.nutikorvalpha;

/**
 * Created by ANDREAS on 12.07.2016.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;


public class InnerBasketFragment extends Fragment {
    Activity mActivity;

    ListView basketView;
    TextView selverPrice;
    TextView maximaPrice;
    TextView prismaPrice;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_basket_inner, container, false);

        basketView = (ListView) rootView.findViewById(R.id.basketView);

//        final ArrayList<String> list = new ArrayList<String>();
//        if (GlobalParameters.b != null && GlobalParameters.b.getAllProducts() != null) {
//            for (int i = 0; i < GlobalParameters.b.getAllProductsArray().length; ++i) {
//                list.add(GlobalParameters.b.getAllProductsArray()[i].toString());
//            }
//        }
//
//        basketView.setAdapter(new CustomListAdapter(mActivity, R.layout.basket_item, list));

        selverPrice = (TextView) rootView.findViewById(R.id.selverPrice);

        prismaPrice = (TextView) rootView.findViewById(R.id.prismaPrice);

        maximaPrice = (TextView) rootView.findViewById(R.id.maximaPrice);


        if (GlobalParameters.b != null && GlobalParameters.b.getAllProducts() != null) {
            selverPrice.setText("Selver: " + String.format("%.2f", GlobalParameters.b.getSelverPrice()) + "€");
            prismaPrice.setText("Prisma: " + String.format("%.2f", GlobalParameters.b.getPrismaPrice()) + "€");
            maximaPrice.setText("Maxima: " + String.format("%.2f", GlobalParameters.b.getMaximaPrice()) + "€");
        } else {
            selverPrice.setText("Selver: " + 0 + "€");
            maximaPrice.setText("Maxima: " + 0 + "€");
            prismaPrice.setText("Prisma: " + 0 + "€");
        }



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        basketView.setAdapter(new CustomListAdapter(mActivity, R.layout.basket_item, GlobalParameters.b.getAllProducts()));

        if (GlobalParameters.b != null && GlobalParameters.b.getAllProducts() != null) {
            selverPrice.setText("Selver: " + String.format("%.2f", GlobalParameters.b.getSelverPrice()) + "€");
            prismaPrice.setText("Prisma: " + String.format("%.2f", GlobalParameters.b.getPrismaPrice()) + "€");
            maximaPrice.setText("Maxima: " + String.format("%.2f", GlobalParameters.b.getMaximaPrice()) + "€");
        } else {
            selverPrice.setText("Selver: " + 0 + "€");
            maximaPrice.setText("Maxima: " + 0 + "€");
            prismaPrice.setText("Prisma: " + 0 + "€");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

//    @Override
//    public void onBackPressed() {
//        Intent myIntent = new Intent(BasketActivity.this, MainActivity.class);
//        BasketActivity.this.startActivity(myIntent);
//        BasketActivity.this.finish(); Todo: fix this
//    }

    private class CustomListAdapter extends ArrayAdapter<Product> {

        private int layout;

        private List<Product> mObjects;

        private Context context;

        public CustomListAdapter(Context context, int resource, List<Product> products) {
            super(context, resource, products);
            this.context = context;
            mObjects = products;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.productImage);
                viewHolder.thumbnail.setAnimation(null);
                UrlImageViewHelper.setUrlDrawable(viewHolder.thumbnail, getItem(position).getImgURL());
                viewHolder.title = (TextView) convertView.findViewById(R.id.productName);
                viewHolder.button = (Button) convertView.findViewById(R.id.removeProduct);
//                viewHolder.EAN = (TextView) convertView.findViewById(R.id.productEAN);
                viewHolder.price = (TextView) convertView.findViewById(R.id.productPrice);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Item removed!", Toast.LENGTH_SHORT).show();
                    GlobalParameters.b.removeProduct(position);
//                    remove(getItem(position));
                    notifyDataSetChanged();
                    System.out.println(position);
                    System.out.println(GlobalParameters.b.getAllProducts());
                    UpdatePrice();
                }
            });
            mainViewholder.title.setText(getItem(position).getName());
//            mainViewholder.EAN.setText("bar code " + getItem(position).getEAN());
            mainViewholder.price.setText("Price: " + getItem(position).getLowestPrice());


            return convertView;
        }

        public void UpdatePrice(){
            TextView txtView = (TextView) ((Activity)context).findViewById(R.id.selverPrice);
            txtView.setText("Selver: " + String.format("%.2f", GlobalParameters.b.getSelverPrice()) + "€");

            TextView txt2View = (TextView) ((Activity)context).findViewById(R.id.prismaPrice);
            txt2View.setText("Prisma: " + String.format("%.2f", GlobalParameters.b.getPrismaPrice()) + "€");

            TextView txt3View = (TextView) ((Activity)context).findViewById(R.id.maximaPrice);
            txt3View.setText("Maxima: " + String.format("%.2f", GlobalParameters.b.getMaximaPrice()) + "€");

        }

    }

    public class ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView EAN;
        TextView price;
        Button button;
    }
}
