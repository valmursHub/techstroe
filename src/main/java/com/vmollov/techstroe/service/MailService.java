package com.vmollov.techstroe.service;

import com.vmollov.techstroe.model.service.OrderServiceModel;

public interface MailService {

    void sendEmail(OrderServiceModel orderServiceModel);
}