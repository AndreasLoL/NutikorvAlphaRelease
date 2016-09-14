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
import java.util.LinkedHashMap;
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
        View rootView = inflater.inflate(R.layout.fragment_basket_inner_new, container, false);

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
            selverPrice.setText(String.format("%.2f", GlobalParameters.b.getSelverPrice()) + "€");
            prismaPrice.setText(String.format("%.2f", GlobalParameters.b.getPrismaPrice()) + "€");
            maximaPrice.setText(String.format("%.2f", GlobalParameters.b.getMaximaPrice()) + "€");
        } else {
            selverPrice.setText(0 + "€");
            maximaPrice.setText(0 + "€");
            prismaPrice.setText(0 + "€");
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

        public CustomListAdapter(Context context, int resource, List<Product> products, LinkedHashMap<Product, Integer> mObjects) {
            super(context, resource, products);
            this.context = context;
            this.mObjects = mObjects;
            this.layout = resource;
            this.listProducts = products;

        }

        private void setPriceColors(Product currentProduct, ViewHolder childViewHolder) {
            childViewHolder.selverPiecePrice.setVisibility(View.VISIBLE);
            childViewHolder.maximaPiecePrice.setVisibility(View.VISIBLE);
            childViewHolder.prismaPiecePrice.setVisibility(View.VISIBLE);
            childViewHolder.selverPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
            childViewHolder.maximaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
            childViewHolder.prismaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
            childViewHolder.selverPiecePrice.setBackgroundResource(R.color.cheap);
            childViewHolder.maximaPiecePrice.setBackgroundResource(R.color.colorPrimaryLight);
            childViewHolder.prismaPiecePrice.setBackgroundResource(R.color.colorPrimaryLight);

            int count = 0;

            //selver 1, prisma 2, maxima 3

            List<Double> tempList = new ArrayList<>();

            Double selverP = currentProduct.getSelverPrice();

            if (selverP.equals(0.0)) {
                selverP = 999.99;
                count += 1;
            }

            Double maximaP = currentProduct.getMaximaPrice();

            if (maximaP.equals(0.0)) {
                maximaP = 999.99;
                count += 1;
            }

            Double prismaP = currentProduct.getPrismaPrice();

            if (prismaP.equals(0.0)) {
                prismaP = 999.99;
                count += 1;
            }

            tempList.addAll(Arrays.asList(selverP, maximaP, prismaP));

            Collections.sort(tempList);

            if (count == 2) {
                childViewHolder.prismaPiecePrice.setVisibility(View.INVISIBLE);
                childViewHolder.maximaPiecePrice.setVisibility(View.INVISIBLE);

                if (tempList.get(0).equals(prismaP)) {
                    childViewHolder.selverPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                } else if (tempList.get(0).equals(selverP)) {
                    childViewHolder.selverPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                } else {
                    childViewHolder.selverPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                }
            } else if (count == 1) {
                childViewHolder.maximaPiecePrice.setVisibility(View.INVISIBLE);
                if (tempList.get(0).equals(tempList.get(1))) {
                    childViewHolder.selverPiecePrice.setBackgroundResource(R.color.colorPrimary);
                    if (tempList.get(0).equals(selverP) && tempList.get(0).equals(maximaP)) {
                        childViewHolder.selverPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        childViewHolder.prismaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    } else if (tempList.get(0).equals(selverP) && tempList.get(0).equals(prismaP)) {
                        childViewHolder.selverPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        childViewHolder.prismaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    } else {
                        childViewHolder.selverPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        childViewHolder.prismaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    }
                } else {
                    if (tempList.get(0).equals(selverP)) {
                        childViewHolder.selverPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        if (tempList.get(1).equals(maximaP)) {
                            childViewHolder.prismaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        } else {
                            childViewHolder.prismaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        }
                    } else if (tempList.get(0).equals(maximaP)) {
                        childViewHolder.selverPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        if (tempList.get(1).equals(selverP)) {
                            childViewHolder.prismaPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        } else {
                            childViewHolder.prismaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        }
                    } else {
                        childViewHolder.selverPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        if (tempList.get(1).equals(selverP)) {
                            childViewHolder.prismaPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        } else {
                            childViewHolder.prismaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        }
                    }
                }
            } else {
                if (!tempList.get(0).equals(tempList.get(1))) {
                    if (tempList.get(0).equals(selverP)) {
                        childViewHolder.selverPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        if (tempList.get(1).equals(maximaP)) {
                            childViewHolder.prismaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                            childViewHolder.maximaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        } else {
                            childViewHolder.prismaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                            childViewHolder.maximaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        }
                    } else if (tempList.get(0).equals(maximaP)) {
                        childViewHolder.selverPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        if (tempList.get(1).equals(selverP)) {
                            childViewHolder.prismaPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                            childViewHolder.maximaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        } else {
                            childViewHolder.prismaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                            childViewHolder.maximaPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        }
                    } else {
                        childViewHolder.selverPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        if (tempList.get(1).equals(selverP)) {
                            childViewHolder.prismaPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                            childViewHolder.maximaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        } else {
                            childViewHolder.prismaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                            childViewHolder.maximaPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        }
                    }
                } else {
                    childViewHolder.selverPiecePrice.setBackgroundResource(R.color.colorPrimary);
                    if (tempList.get(0).equals(tempList.get(1)) && tempList.get(0).equals(tempList.get(2))) {
                        childViewHolder.prismaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        childViewHolder.selverPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        childViewHolder.maximaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    } else {
                        if (tempList.get(0).equals(selverP) && tempList.get(0).equals(maximaP)) {
                            childViewHolder.maximaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                            childViewHolder.selverPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                            childViewHolder.prismaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        } else if (tempList.get(0).equals(selverP) && tempList.get(0).equals(prismaP)) {
                            childViewHolder.maximaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                            childViewHolder.selverPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                            childViewHolder.prismaPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        } else {
                            childViewHolder.maximaPiecePrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                            childViewHolder.selverPiecePrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                            childViewHolder.prismaPiecePrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        }
                    }
                }
            }
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
                    GlobalParameters.b.removeProduct(getItem(position));;
                    remove(getItem(position));
                    notifyDataSetChanged();
                    UpdatePrice();
                }
            });
            
            mainViewholder.title.setText(getItem(position).getName());
            mainViewholder.price.setText(getItem(position).getPriceRange(mObjects.get(getItem(position))));
            mainViewholder.quantity.setText("qty: " + mObjects.get(getItem(position)));

            setPriceColors(getItem(position), mainViewholder);


            return convertView;
        }

        public void UpdatePrice(){
            if (GlobalParameters.b != null && GlobalParameters.b.getAllProducts() != null) {
                selverPrice.setText(String.format("%.2f", GlobalParameters.b.getSelverPrice()) + "€");
                prismaPrice.setText(String.format("%.2f", GlobalParameters.b.getPrismaPrice()) + "€");
                maximaPrice.setText(String.format("%.2f", GlobalParameters.b.getMaximaPrice()) + "€");
            } else {
                selverPrice.setText(0 + "€");
                maximaPrice.setText(0 + "€");
                prismaPrice.setText(0 + "€");
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
