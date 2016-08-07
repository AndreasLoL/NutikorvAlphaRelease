package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
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
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Objects.SubcategoryParentListItem;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

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

    @Override
    public void onBindChildViewHolder(final MyChildViewHolder childViewHolder, final int position, final Object childListItem) {
        Product subcategoryChildListItem = (Product) childListItem;
        childViewHolder.productName.setText(subcategoryChildListItem.getName());
        childViewHolder.selverPrice.setText("Selver: " + String.format("%.2f", subcategoryChildListItem.getSelverPrice()) + "€");
        childViewHolder.maximaPrice.setText("Maxima: " + String.format("%.2f", subcategoryChildListItem.getMaximaPrice()) + "€");
        childViewHolder.prismaPrice.setText("Prisma: " + String.format("%.2f", subcategoryChildListItem.getPrismaPrice()) + "€");
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