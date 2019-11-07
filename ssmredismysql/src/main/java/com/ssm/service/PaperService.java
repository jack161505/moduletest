package com.ssm.service;

import com.ssm.pojo.Paper;

import java.util.List;

/**
 * @Author: 李善玺
 * @Date: 2019/11/6 21:11
 * @Version 1.0
 */
public interface PaperService {

    int addPaper(Paper paper);

    int deletePaperById(long id);

    int updatePaper(Paper paper);

    Paper queryById(long id);

    List<Paper> queryAllPaper();
}
