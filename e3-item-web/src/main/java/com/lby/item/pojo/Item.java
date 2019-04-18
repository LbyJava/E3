package com.lby.item.pojo;
import java.util.Date;

import com.lby.pojo.TbItem;

import java.io.Serializable;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/21 23:40
 */
public class Item extends TbItem implements Serializable {
    public Item(TbItem tbItem) {
        this.setId(tbItem.getId());
        this.setTitle(tbItem.getTitle());
        this.setSellPoint(tbItem.getSellPoint());
        this.setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
        this.setImage(tbItem.getImage());
        this.setCid(tbItem.getCid());
        this.setStatus(tbItem.getStatus());
        this.setCreated(tbItem.getCreated());
        this.setUpdated(tbItem.getUpdated());


    }

    public Item() {
    }

    public String[] getImages() {
        String image = this.getImage();
        if (image != null && !"".equals(image)) {
            String[] split = image.split(",");
            return split;
        }
        return null;
    }

}
