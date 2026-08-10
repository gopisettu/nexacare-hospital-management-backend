package com.nexacare.hospital.controller;


import com.nexacare.hospital.dto.request.medicinereq.AddMedicineDto;
import com.nexacare.hospital.service.StaffService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
@AllArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @PostMapping("/addMedicineStock-ByStaff/{username}")
    public void  addMedicine(@PathVariable String username,@RequestBody AddMedicineDto addMedicineBatchDto ){
        staffService.addMedicine(username,addMedicineBatchDto);
    }

}
