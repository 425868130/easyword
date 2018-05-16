package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.mapperInterface.ListeningMapper;
import com.nchu.easyword.dao.model.Listening;
import com.nchu.easyword.service.inface.ListenService;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/*听力相关业务*/
public class ListenServiceImpl implements ListenService {
    @Autowired
    ListeningMapper listeningMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return listeningMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Listening record) {
        return listeningMapper.insert(record);
    }

    @Override
    public int insertSelective(Listening record) {
        return listeningMapper.insertSelective(record);
    }

    @Override
    public Listening selectByPrimaryKey(Long id) {
        return listeningMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Listening record) {
        record.setGmtModified(DateUtil.getCurrentTimestamp());
        return listeningMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Listening record) {
        record.setGmtModified(DateUtil.getCurrentTimestamp());
        return listeningMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(Listening record) {
        record.setGmtModified(DateUtil.getCurrentTimestamp());
        return listeningMapper.updateByPrimaryKey(record);
    }

    @Override
    public long getCount() {
        return listeningMapper.getCount();
    }

    @Override
    public List<Listening> getByPage(int page, int pageSize) {
        return listeningMapper.getByPage((page - 1) * pageSize, pageSize);
    }
}
