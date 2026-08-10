package com.nexacare.hospital.service;


import com.nexacare.hospital.dto.request.medicinereq.AddMedicineDto;
import com.nexacare.hospital.dto.request.authreq.LoginDto;
import com.nexacare.hospital.enums.BatchStatus;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.exception.ResourceNotFoundException;
import com.nexacare.hospital.mapper.dtotoentity.MedicineBatchMapper;
import com.nexacare.hospital.mapper.dtotoentity.MedicineMapper;
import com.nexacare.hospital.model.Medicine;
import com.nexacare.hospital.model.MedicineBatch;
import com.nexacare.hospital.model.Staff;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StaffService {

    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;
    private  final StaffRepository staffRepository;
    private  final JwtService jwtService;
    private final MedicineBatchRepository medicineBatchRepository;
    private final MedicineBatchMapper medicineBatchMapper;
    private final MedicineRepository medicineRepository;
    private  final MedicineMapper medicineMapper;
    public void registerStaff(@Valid LoginDto loginDto) {
        Staff staff=new Staff();
        User user=new User();
        user.setUsername(loginDto.username());
        user.setPassword(passwordEncoder.encode(loginDto.password()));
        user.setRole(Role.STAFF);
        userRepository.save(user);
        staff.setUser(user);
        staff.setFullname(loginDto.username());
        staff.setRole(Role.STAFF);
        staffRepository.save(staff);
        log.info("Staff Successfully Registedred  ");

    }

//    public TokenDto loginStaff(LoginDto loginDto) {
//        return  jwtService.generateToken(loginDto.username());
//    }


    public void addMedicine(String username, AddMedicineDto medicineDto) {
        userRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Staff not found"));


        Medicine medicine = medicineMapper.mapDtoToMedicine(medicineDto);
        medicineRepository.save(medicine);

        MedicineBatch batch = medicineMapper.mapDtoToMedicineBatch(medicineDto);
        batch.setMedicine(medicine);
        batch.setBatchStatus(BatchStatus.ACTIVE);

        medicineBatchRepository.save(batch);

    }
}
