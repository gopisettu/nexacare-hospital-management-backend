package com.nexacare.hospital.mapper.dtotoentity;

import com.nexacare.hospital.dto.request.AddMedicineBatchDto;
import com.nexacare.hospital.model.MedicineBatch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MedicineBatchMapper {


    public MedicineBatch mapDtoToEntity(AddMedicineBatchDto dto) {

        MedicineBatch batch = new MedicineBatch();

        batch.setBatchNo(dto.batchNo());
        batch.setQuantityReceived(dto.quantityReceived());
        batch.setQuantityRemaining(dto.quantityReceived()); // Initially same as received quantity
        batch.setExpiryDate(dto.expiryDate());

        // Medicine and BatchStatus are set in the service

        return batch;
    }
}
