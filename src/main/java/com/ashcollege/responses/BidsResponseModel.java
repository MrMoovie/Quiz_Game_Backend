package com.ashcollege.responses;

import com.ashcollege.entities.BidEntity;

import java.util.List;

public class BidsResponseModel extends BasicResponse {
    private List<BidModel> bids;


    public BidsResponseModel() {
    }

    public BidsResponseModel(boolean success, Integer errorCode, List<BidEntity> bidEntities) {
        super(success, errorCode);
        this.bids = bidEntities.stream().map(BidModel::new).toList();
    }

    public List<BidModel> getBids() {
        return bids;
    }

    public void setBids(List<BidModel> bids) {
        this.bids = bids;
    }
}
