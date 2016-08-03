package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Objects.MainCategory;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * Created by ANDREAS on 17.07.2016.
 */
public class ExpandableRecyclerViewAdapter extends ExpandableRecyclerAdapter<ExpandableRecyclerViewAdapter.ProductsCategorizer> {
    public static final int TYPE_PERSON = 1001;
    OnCardClickListner onCardClickListner;
    List<ProductsCategorizer> products;

    public ExpandableRecyclerViewAdapter(Context context, MainCategory c) {
        super(context);

        this.products = hashmapToProducts(c.getHashMapProducts());

        setItems(products);

    }

    public List<ProductsCategorizer> hashmapToProducts(HashMap<String, List<Product>> products) {
        List<ProductsCategorizer> temp = new ArrayList<>();

        for (String s : products.keySet()) {
            temp.add(new ProductsCategorizer(s));
            for (Product p : products.get(s)) {
                temp.add(new ProductsCategorizer(p));
            }
        }

        return temp;
    }

    public static class ProductsCategorizer extends ExpandableRecyclerAdapter.ListItem {
        public String Text;
        public Product p;

        public String getText() {
            return Text;
        }

        public ProductsCategorizer(String group) {
            super(TYPE_HEADER);

            Text = group;
        }

        public Product getP() {
            return p;
        }

        public ProductsCategorizer(Product p) {
            super(TYPE_PERSON);

            this.p = p;
        }
    }


    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;

        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));

            name = (TextView) view.findViewById(R.id.item_header_name);
        }

        public void bind(int position) {
            super.bind(position);

            name.setText(visibleItems.get(position).getText());
        }
    }

    public class ProductViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        public TextView getName() {
            return name;
        }

        public Button getAddButton() {
            return add;
        }

        public EditText getQuantity() {
            return quantity;
        }

        TextView name;


        TextView selverPrice;

        TextView maximaPrice;

        TextView prismaPrice;

        ImageView img;

        Button add;

        EditText quantity;

        public ProductViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.item_name);

            selverPrice = (TextView) view.findViewById(R.id.selverPrice);

            maximaPrice = (TextView) view.findViewById(R.id.maximaPrice);

            prismaPrice = (TextView) view.findViewById(R.id.prismaPrice);

            img = (ImageView) view.findViewById(R.id.item_image);

            add = (Button) view.findViewById(R.id.addButton);

            quantity = (EditText) view.findViewById(R.id.quantityField);
        }

        public void bind(int position) {
            name.setText(visibleItems.get(position).getP().getName());


            Double selver = visibleItems.get(position).getP().getSelverPrice();
            Double maxima = visibleItems.get(position).getP().getMaximaPrice();
            Double prisma = visibleItems.get(position).getP().getPrismaPrice();

            prismaPrice.setVisibility(View.VISIBLE);
            selverPrice.setVisibility(View.VISIBLE);
            maximaPrice.setVisibility(View.VISIBLE);


            if (selver.equals(0.0)) {
                selver = 999.9;
            }

            if (maxima.equals(0.0)) {
                maxima = 999.9;
            }

            if (prisma.equals(0.0)) {
                prisma = 999.9;
            }


            List<Double> t1 = new ArrayList<>();

            t1.addAll(Arrays.asList(selver, prisma, maxima));
            Collections.sort(t1);

            if (Double.compare(selver, maxima) == 0 && Double.compare(prisma, maxima) == 0) {
                selverPrice.setBackgroundResource(R.color.cheap);
                maximaPrice.setBackgroundResource(R.color.cheap);
                prismaPrice.setBackgroundResource(R.color.cheap);
            } else if (Double.compare(t1.get(0), t1.get(1)) == 0) {
                if ((Double.compare(t1.get(0), selver) == 0) && (Double.compare(t1.get(0), maxima) == 0)) {
                    selverPrice.setBackgroundResource(R.color.cheap);
                    maximaPrice.setBackgroundResource(R.color.cheap);
                    prismaPrice.setBackgroundResource(R.color.medium);
                    //maxima selver green
                    //prisma yellow
                } else if ((Double.compare(t1.get(0), prisma) == 0) && (Double.compare(t1.get(0), maxima) == 0)) {
                    maximaPrice.setBackgroundResource(R.color.cheap);
                    prismaPrice.setBackgroundResource(R.color.cheap);
                    selverPrice.setBackgroundResource(R.color.medium);
                    //maxima prisma green
                    //selver yellow
                } else {
                    selverPrice.setBackgroundResource(R.color.cheap);
                    prismaPrice.setBackgroundResource(R.color.cheap);
                    maximaPrice.setBackgroundResource(R.color.medium);
                    //prisma selver green
                    //maxima yellow
                }

            } else if (Double.compare(t1.get(1), t1.get(2)) == 0) {
                if ((Double.compare(t1.get(0), selver) == 0)) {
                    selverPrice.setBackgroundResource(R.color.cheap);
                    maximaPrice.setBackgroundResource(R.color.medium);
                    prismaPrice.setBackgroundResource(R.color.medium);
                    //selver green
                } else if ((Double.compare(t1.get(0), prisma) == 0)) {
                    //prisma green
                    prismaPrice.setBackgroundResource(R.color.cheap);
                    selverPrice.setBackgroundResource(R.color.medium);
                    maximaPrice.setBackgroundResource(R.color.medium);
                } else {
                    //maxima green
                    maximaPrice.setBackgroundResource(R.color.cheap);
                    selverPrice.setBackgroundResource(R.color.medium);
                    prismaPrice.setBackgroundResource(R.color.medium);
                }

            } else {
                if ((Double.compare(t1.get(0), selver) == 0)) {
                    selverPrice.setBackgroundResource(R.color.cheap);

                    if ((Double.compare(t1.get(1), prisma) == 0)) {
                        prismaPrice.setBackgroundResource(R.color.medium);
                        maximaPrice.setBackgroundResource(R.color.expensive);
                    } else {
                        prismaPrice.setBackgroundResource(R.color.expensive);
                        maximaPrice.setBackgroundResource(R.color.medium);
                    }
                } else if ((Double.compare(t1.get(0), maxima) == 0)) {
                    maximaPrice.setBackgroundResource(R.color.cheap);
                    if ((Double.compare(t1.get(1), prisma) == 0)) {
                        prismaPrice.setBackgroundResource(R.color.medium);
                        selverPrice.setBackgroundResource(R.color.expensive);
                    } else{
                        prismaPrice.setBackgroundResource(R.color.expensive);
                        selverPrice.setBackgroundResource(R.color.medium);
                    }
                } else {
                    prismaPrice.setBackgroundResource(R.color.cheap);
                    if ((Double.compare(t1.get(1), maxima) == 0)) {
                        maximaPrice.setBackgroundResource(R.color.medium);
                        selverPrice.setBackgroundResource(R.color.expensive);
                    } else {
                        maximaPrice.setBackgroundResource(R.color.expensive);
                        selverPrice.setBackgroundResource(R.color.medium);
                    }
                }
            }



            selverPrice.setText("Selver: " + String.format("%.2f", selver) + "€");
            prismaPrice.setText("Prisma: " + String.format("%.2f", prisma) + "€");
            maximaPrice.setText("Maxima: " + String.format("%.2f", maxima) + "€");


            if (selver.equals(999.9)) {
                selverPrice.setVisibility(View.INVISIBLE);
            }

            if (maxima.equals(999.9)) {
                maximaPrice.setVisibility(View.INVISIBLE);
            }

            if (prisma.equals(999.9)) {
                prismaPrice.setVisibility(View.INVISIBLE);
            }
            UrlImageViewHelper.setUrlDrawable(img, visibleItems.get(position).getP().getImgURL());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.item_header, parent));
            case TYPE_PERSON:
            default:
                return new ProductViewHolder(inflate(R.layout.item_product, parent));
        }
    }



    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_PERSON:
            default:
                if (holder instanceof ProductViewHolder) {
                    final ProductViewHolder temp = (ProductViewHolder) holder;

                    temp.getAddButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int qt;
                            try {
                                qt = Integer.parseInt(temp.getQuantity().getText().toString());
                            } catch (Exception e) {
                                qt = 1;
                            }
                            GlobalParameters.b.addToBasket(products.get(position).getP(), qt);
                            InputMethodManager imm = (InputMethodManager) v.getContext()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    });
                }

                ((ProductViewHolder) holder).bind(position);
                break;
        }

    }

    public interface OnCardClickListner {
        void OnCardClicked(View view, int position);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }
}
