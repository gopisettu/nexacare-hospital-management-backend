package com.nexacare.hospital.service;

import com.nexacare.hospital.enums.PaymentStatus;
import com.nexacare.hospital.model.Appointment;
import com.nexacare.hospital.model.Doctor;
import com.nexacare.hospital.model.Medicine;
import org.springframework.stereotype.Service;
@Service
public class BillingService {

    public double calculateMedicineCost(
            Medicine medicine,
            Integer quantity){

        return medicine.getUnitPrice() * quantity;
    }

    public void generateBill(
            Appointment appointment,
            Doctor doctor,
            double medicineFee){

        appointment.setConsultationFee(
                doctor.getConsultationFee());

        appointment.setMedicineFee(medicineFee);

        appointment.setTotalBill(
                doctor.getConsultationFee()
                        + medicineFee);

        appointment.setPaymentStatus(
                PaymentStatus.PENDING);
    }
}