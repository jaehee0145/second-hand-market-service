package com.itembay.service;

import com.itembay.domain.Item;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.dto.ItemSearchReqData;
import org.springframework.data.domain.Page;

public interface ItemService {

    /**
     * 아이템 등록
     * @param itemRegisterReqData 아이템 등록 요청 데이터
     * @return Item
     */
    Item registerItem(ItemRegisterReqData itemRegisterReqData);

    /**
     * 아이템 조회
     *
     * @param itemSearchReqData 아이템 조회 데이터
     * @return Item
     */
    Page<Item> searchItem(ItemSearchReqData itemSearchReqData);
}
