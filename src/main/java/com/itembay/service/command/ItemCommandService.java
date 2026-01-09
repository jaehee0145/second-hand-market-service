package com.itembay.service.command;

import com.itembay.domain.Item;
import com.itembay.dto.ItemRegisterReqData;

public interface ItemCommandService {

    /**
     * 아이템 등록
     * @param itemRegisterReqData 아이템 등록 요청 데이터
     * @return Item
     */
    Item registerItem(ItemRegisterReqData itemRegisterReqData);

}
