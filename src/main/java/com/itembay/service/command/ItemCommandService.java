package com.itembay.service.command;

import com.itembay.domain.Item;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.dto.ItemUpdateReqData;

public interface ItemCommandService {

    /**
     * 아이템 등록
     * @param itemRegisterReqData 아이템 등록 요청 데이터
     * @return Item
     */
    Item registerItem(ItemRegisterReqData itemRegisterReqData);

    /**
     * 아이템 수정
     * @param itemUpdateReqData 아이템 수정 요청 데이터
     */
    void updateItem(ItemUpdateReqData itemUpdateReqData);

}
