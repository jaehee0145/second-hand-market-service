package com.itembay.service.query;

import com.itembay.domain.Item;
import com.itembay.dto.ItemSearchReqData;
import org.springframework.data.domain.Page;

public interface ItemQueryService {

    /**
     * 아이템 조회
     *
     * @param itemSearchReqData 아이템 조회 데이터
     * @return Item
     */
    Page<Item> searchItem(ItemSearchReqData itemSearchReqData);
}
