package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Fragments.InnerMainFragment;
import com.nutikorv.andreas.nutikorvalpha.MainActivity;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.List;

/**
 * Created by ANDREAS on 15.07.2016.
 */
public class ProductListAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    List<Product> mylist;
    InnerMainFragment f;

    public ProductListAdapter(Context _context, List<Product> _mylist, InnerMainFragment f) {
        super(_context, R.layout.grid_item, _mylist);
        mContext = _context;
        this.mylist = _mylist;
        this.f = f;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);

        ProductViewHolder holder;

        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.grid_item, parent, false);

            //
            holder = new ProductViewHolder();
            holder.img = (ImageView)convertView.findViewById(R.id.image);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.p1 = (TextView) convertView.findViewById(R.id.price1);
            holder.p2 = (TextView) convertView.findViewById(R.id.price2);
            holder.p3 = (TextView) convertView.findViewById(R.id.price3);

            //
            convertView.setTag(holder);
        }
        else{
            holder = (ProductViewHolder) convertView.getTag();
        }


        //
        holder.populate(product);


        return convertView;
    }


    static class ProductViewHolder {
        public ImageView img;
        public TextView title;
        public TextView p1;
        public TextView p2;
        public TextView p3;
        public Button add;

        void populate(Product p) {
            title.setText(p.getName());
            UrlImageViewHelper.setUrlDrawable(img, p.getImgURL());
        }

        void populate(Product p, boolean isBusy) {
            title.setText(p.getName());
            p1.setText("Prisma: " + String.format("%.2f", p.getPrismaPrice()) + "€");
            p2.setText("Selver: " + String.format("%.2f", p.getSelverPrice()) + "€");
            p3.setText("Maxima: " + String.format("%.2f", p.getMaximaPrice()) + "€");


            if (!isBusy){
                // download from internet
                UrlImageViewHelper.setUrlDrawable(img, p.getImgURL());
            }
            else{
                // set default image
                img.setImageResource(R.color.colorPrimary);
            }
        }
    }

}