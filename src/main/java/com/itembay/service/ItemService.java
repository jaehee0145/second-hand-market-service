package com.itembay.service;

import com.itembay.domain.Item;
import com.itembay.dto.ItemRegisterReqData;

public interface ItemService {

    /**
     * 아이템 등록
     * @param itemRegisterReqData 아이템 등록 요청 데이터
     * @return Item
     */
    Item registerItem(ItemRegisterReqData itemRegisterReqData);
}
