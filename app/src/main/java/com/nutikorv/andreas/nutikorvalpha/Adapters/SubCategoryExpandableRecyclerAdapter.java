package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Objects.SubcategoryParentListItem;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ANDREAS on 04.08.2016.
 */
public class SubCategoryExpandableRecyclerAdapter extends ExpandableRecyclerAdapter<SubCategoryExpandableRecyclerAdapter.MyParentViewHolder, SubCategoryExpandableRecyclerAdapter.MyChildViewHolder> {
    private LayoutInflater mInflater;

    public SubCategoryExpandableRecyclerAdapter(Context context, List<ParentListItem> itemList) {
        super(itemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.item_header, viewGroup, false);
        return new MyParentViewHolder(view);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.item_product, viewGroup, false);
        return new MyChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(MyParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        SubcategoryParentListItem subcategoryParentListItem = (SubcategoryParentListItem) parentListItem;
        parentViewHolder.lblListHeader.setText(subcategoryParentListItem.mTitle);

    }

    private void setPriceColors(Product currentProduct, MyChildViewHolder childViewHolder) {
        childViewHolder.selverPrice.setVisibility(View.VISIBLE);
        childViewHolder.maximaPrice.setVisibility(View.VISIBLE);
        childViewHolder.prismaPrice.setVisibility(View.VISIBLE);
        childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
        childViewHolder.maximaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
        childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
        childViewHolder.selverPrice.setBackgroundResource(R.color.cheap);
        childViewHolder.maximaPrice.setBackgroundResource(R.color.colorPrimary);
        childViewHolder.prismaPrice.setBackgroundResource(R.color.colorPrimary);

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
            childViewHolder.prismaPrice.setVisibility(View.INVISIBLE);
            childViewHolder.maximaPrice.setVisibility(View.INVISIBLE);

            if (tempList.get(0).equals(prismaP)) {
                childViewHolder.selverPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
            } else if (tempList.get(0).equals(selverP)) {
                childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
            } else {
                childViewHolder.selverPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
            }
        } else if (count == 1) {
            childViewHolder.maximaPrice.setVisibility(View.INVISIBLE);
            if (tempList.get(0).equals(tempList.get(1))) {
                childViewHolder.selverPrice.setBackgroundResource(R.color.colorPrimary);
                if (tempList.get(0).equals(selverP) && tempList.get(0).equals(maximaP)) {
                    childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                    childViewHolder.prismaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                } else if (tempList.get(0).equals(selverP) && tempList.get(0).equals(prismaP)) {
                    childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                    childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                } else {
                    childViewHolder.selverPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                }
            } else {
                if (tempList.get(0).equals(selverP)) {
                    childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                    if (tempList.get(1).equals(maximaP)) {
                        childViewHolder.prismaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    } else {
                        childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    }
                } else if (tempList.get(0).equals(maximaP)) {
                    childViewHolder.selverPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    if (tempList.get(1).equals(selverP)) {
                        childViewHolder.prismaPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                    } else {
                        childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    }
                } else {
                    childViewHolder.selverPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    if (tempList.get(1).equals(selverP)) {
                        childViewHolder.prismaPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                    } else {
                        childViewHolder.prismaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    }
                }
            }
        } else {
            if (!tempList.get(0).equals(tempList.get(1))) {
                if (tempList.get(0).equals(selverP)) {
                    childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                    if (tempList.get(1).equals(maximaP)) {
                        childViewHolder.prismaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        childViewHolder.maximaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    } else {
                        childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        childViewHolder.maximaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    }
                } else if (tempList.get(0).equals(maximaP)) {
                    childViewHolder.selverPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    if (tempList.get(1).equals(selverP)) {
                        childViewHolder.prismaPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        childViewHolder.maximaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    } else {
                        childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        childViewHolder.maximaPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                    }
                } else {
                    childViewHolder.selverPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    if (tempList.get(1).equals(selverP)) {
                        childViewHolder.prismaPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        childViewHolder.maximaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    } else {
                        childViewHolder.prismaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        childViewHolder.maximaPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                    }
                }
            } else {
                childViewHolder.selverPrice.setBackgroundResource(R.color.colorPrimary);
                if (tempList.get(0).equals(tempList.get(1)) && tempList.get(0).equals(tempList.get(2))) {
                    childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                    childViewHolder.maximaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                } else {
                    if (tempList.get(0).equals(selverP) && tempList.get(0).equals(maximaP)) {
                        childViewHolder.maximaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        childViewHolder.prismaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    } else if (tempList.get(0).equals(selverP) && tempList.get(0).equals(prismaP)) {
                        childViewHolder.maximaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                        childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                    } else {
                        childViewHolder.maximaPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
                        childViewHolder.selverPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
                        childViewHolder.prismaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
                    }
                }
            }
        }


//        if (!tempList.get(0).equals(tempList.get(1))) {
//            if (tempList.get(0).equals(prismaP)) {
//                childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", currentProduct.getPrismaPrice()) + "€");
//            } else if (tempList.get(0).equals(selverP)) {
//                childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", currentProduct.getSelverPrice()) + "€");
//            } else {
//                childViewHolder.maximaPrice.setText("Maxima: " + String.format("%.2f", currentProduct.getMaximaPrice()) + "€");
//            }
//        }


    }

    @Override
    public void onBindChildViewHolder(final MyChildViewHolder childViewHolder, final int position, final Object childListItem) {

        setPriceColors((Product) childListItem, childViewHolder);

        Product subcategoryChildListItem = (Product) childListItem;
        childViewHolder.productName.setText(subcategoryChildListItem.getName());
        UrlImageViewHelper.setUrlDrawable(childViewHolder.img, subcategoryChildListItem.getImgURL());

        childViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qt;
                try {
                    qt = Integer.parseInt(childViewHolder.quantity.getText().toString());
                } catch (Exception e) {
                    qt = 1;
                }
                GlobalParameters.b.addToBasket((Product) childListItem, qt);
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

    }

    public class MyParentViewHolder extends ParentViewHolder {

        public TextView lblListHeader;

        public MyParentViewHolder(View itemView) {
            super(itemView);
            lblListHeader = (TextView) itemView.findViewById(R.id.item_header_name);
        }
    }

    @Override
    public void onParentListItemExpanded(int position) {
        Object parent = mItemList.get(position);
        collapseAllParents();    // Alternatively keep track of the single item that is expanded and explicitly collapse that row (more efficient)
        expandParent(((ParentWrapper) parent).getParentListItem());
    }

    @Override
    public void collapseAllParents() {
        super.collapseAllParents();
    }

    public class MyChildViewHolder extends ChildViewHolder {

        public TextView productName;

        public TextView selverPrice;

        public TextView maximaPrice;

        public TextView prismaPrice;

        public ImageView img;

        public Button add;

        public EditText quantity;


        public MyChildViewHolder(View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.item_name);

            selverPrice = (TextView) itemView.findViewById(R.id.selverPrice);

            maximaPrice = (TextView) itemView.findViewById(R.id.maximaPrice);

            prismaPrice = (TextView) itemView.findViewById(R.id.prismaPrice);

            img = (ImageView) itemView.findViewById(R.id.item_image);

            add = (Button) itemView.findViewById(R.id.addButton);

            quantity = (EditText) itemView.findViewById(R.id.quantityField);
        }
    }
}