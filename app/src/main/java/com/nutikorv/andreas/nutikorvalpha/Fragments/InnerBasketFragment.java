package com.nutikorv.andreas.nutikorvalpha.Fragments;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

        basketView.setAdapter(new CustomListAdapter(mActivity, R.layout.basket_card_item, GlobalParameters.b.getAllHashKeys(),GlobalParameters.b.getAllProducts()));

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

        private HashMap<Product, Integer> mObjects;

        private List<Product> listProducts;

        private Context context;

        public CustomListAdapter(Context context, int resource, List<Product> products, HashMap<Product, Integer> mObjects) {
            super(context, resource, products);
            this.context = context;
            this.mObjects = mObjects;
            this.layout = resource;
            this.listProducts = products;

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
                viewHolder.button = (ImageButton) convertView.findViewById(R.id.deleteProduct);
                viewHolder.price = (TextView) convertView.findViewById(R.id.totalPrice);
                viewHolder.quantity = (TextView) convertView.findViewById(R.id.quantity);
                viewHolder.maximaPiecePrice = (TextView) convertView.findViewById(R.id.maximaPrice);
                viewHolder.selverPiecePrice = (TextView) convertView.findViewById(R.id.selverPrice);
                viewHolder.prismaPiecePrice = (TextView) convertView.findViewById(R.id.prismaPrice);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Item removed!", Toast.LENGTH_SHORT).show();
                    GlobalParameters.b.removeProduct(getItem(position));
                    remove(getItem(position));
                    notifyDataSetChanged();
                    System.out.println(GlobalParameters.b.getAllProducts());
                    UpdatePrice();
                }
            });

            Double selver = getItem(position).getSelverPrice();
            Double maxima = getItem(position).getMaximaPrice();
            Double prisma = getItem(position).getPrismaPrice();


            List<Double> t1 = new ArrayList<>();

            t1.addAll(Arrays.asList(selver, prisma, maxima));
            Collections.sort(t1);

            if (Double.compare(selver, maxima) == 0 && Double.compare(prisma, maxima) == 0) {
                mainViewholder.selverPiecePrice.setBackgroundResource(R.color.cheap);
                mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.cheap);
                mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.cheap);
            } else if (Double.compare(t1.get(0), t1.get(1)) == 0) {
                if ((Double.compare(t1.get(0), selver) == 0) && (Double.compare(t1.get(0), maxima) == 0)) {
                    mainViewholder.selverPiecePrice.setBackgroundResource(R.color.cheap);
                    mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.cheap);
                    mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.medium);
                    //maxima selver green
                    //prisma yellow
                } else if ((Double.compare(t1.get(0), prisma) == 0) && (Double.compare(t1.get(0), maxima) == 0)) {
                    mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.cheap);
                    mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.cheap);
                    mainViewholder.selverPiecePrice.setBackgroundResource(R.color.medium);
                    //maxima prisma green
                    //selver yellow
                } else {
                    mainViewholder.selverPiecePrice.setBackgroundResource(R.color.cheap);
                    mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.cheap);
                    mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.medium);
                    //prisma selver green
                    //maxima yellow
                }

            } else if (Double.compare(t1.get(1), t1.get(2)) == 0) {
                if ((Double.compare(t1.get(0), selver) == 0)) {
                    mainViewholder.selverPiecePrice.setBackgroundResource(R.color.cheap);
                    mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.medium);
                    mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.medium);
                    //selver green
                } else if ((Double.compare(t1.get(0), prisma) == 0)) {
                    //prisma green
                    mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.cheap);
                    mainViewholder.selverPiecePrice.setBackgroundResource(R.color.medium);
                    mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.medium);
                } else {
                    //maxima green
                    mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.cheap);
                    mainViewholder.selverPiecePrice.setBackgroundResource(R.color.medium);
                    mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.medium);
                }

            } else {
                if ((Double.compare(t1.get(0), selver) == 0)) {
                    mainViewholder.selverPiecePrice.setBackgroundResource(R.color.cheap);

                    if ((Double.compare(t1.get(1), prisma) == 0)) {
                        mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.medium);
                        mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.expensive);
                    } else {
                        mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.expensive);
                        mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.medium);
                    }
                } else if ((Double.compare(t1.get(0), maxima) == 0)) {
                    mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.cheap);
                    if ((Double.compare(t1.get(1), prisma) == 0)) {
                        mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.medium);
                        mainViewholder.selverPiecePrice.setBackgroundResource(R.color.expensive);
                    } else{
                        mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.expensive);
                        mainViewholder.selverPiecePrice.setBackgroundResource(R.color.medium);
                    }
                } else {
                    mainViewholder.prismaPiecePrice.setBackgroundResource(R.color.cheap);
                    if ((Double.compare(t1.get(1), maxima) == 0)) {
                        mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.medium);
                        mainViewholder.selverPiecePrice.setBackgroundResource(R.color.expensive);
                    } else {
                        mainViewholder.maximaPiecePrice.setBackgroundResource(R.color.expensive);
                        mainViewholder.selverPiecePrice.setBackgroundResource(R.color.medium);
                    }
                }
            }



            mainViewholder.title.setText(getItem(position).getName());
            mainViewholder.price.setText(getItem(position).getPriceRange(mObjects.get(getItem(position))));
            mainViewholder.quantity.setText("qty: " + mObjects.get(getItem(position)));
            mainViewholder.selverPiecePrice.setText("Selver: " + getItem(position).getSelverPrice());
            mainViewholder.prismaPiecePrice.setText("Prisma: " + getItem(position).getPrismaPrice());
            mainViewholder.maximaPiecePrice.setText("Maxima: " + getItem(position).getMaximaPrice());


            return convertView;
        }

        public void UpdatePrice(){
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

    }

    public class ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView price;
        ImageButton button;
        TextView quantity;
        TextView selverPiecePrice;
        TextView prismaPiecePrice;
        TextView maximaPiecePrice;
    }
}
