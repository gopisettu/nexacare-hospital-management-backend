package com.nexacare.hospital.service;

import com.nexacare.hospital.enums.BatchStatus;
import com.nexacare.hospital.exception.IllegalOperationException;
import com.nexacare.hospital.model.Medicine;
import com.nexacare.hospital.model.MedicineBatch;
import com.nexacare.hospital.repositories.MedicineBatchRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class InventoryService {

    private final MedicineBatchRepository medicineBatchRepository;

    @Transactional
    public void deductStock(Medicine medicine, Integer quantity) {

        List<MedicineBatch> batches =
                medicineBatchRepository.findAvailableBatches(medicine.getId());

        if (batches.isEmpty()) {
            throw new IllegalOperationException(
                    "Medicine is out of stock : " + medicine.getName());
        }

        int requiredQuantity = quantity;

        for (MedicineBatch batch : batches) {

            if (requiredQuantity == 0) {
                break;
            }

            int available = batch.getQuantityRemaining();

            if (available >= requiredQuantity) {

                batch.setQuantityRemaining(available - requiredQuantity);

                if (batch.getQuantityRemaining() == 0) {
                    batch.setBatchStatus(BatchStatus.OUT_OF_STOCK);
                }

                medicineBatchRepository.save(batch);

                requiredQuantity = 0;

            } else {

                requiredQuantity -= available;

                batch.setQuantityRemaining(0);
                batch.setBatchStatus(BatchStatus.OUT_OF_STOCK);

                medicineBatchRepository.save(batch);
            }
        }

        if (requiredQuantity > 0) {
            throw new IllegalOperationException(
                    "Insufficient stock for medicine : " + medicine.getName());
        }
    }
}