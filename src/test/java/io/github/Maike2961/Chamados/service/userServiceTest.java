package io.github.Maike2961.Chamados.service;

import io.github.Maike2961.Chamados.Enums.RolesEnum;
import io.github.Maike2961.Chamados.repository.RolesRepository;
import io.github.Maike2961.Chamados.repository.UsersRepository;
import io.github.Maike2961.Chamados.controller.dto.userRequestDTO;
import io.github.Maike2961.Chamados.models.Roles;
import io.github.Maike2961.Chamados.models.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(MockitoExtension.class)
class userServiceTest {

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private userService service;

    @Captor
    private ArgumentCaptor<Users> usersArgumentCaptor;

    @Captor
    ArgumentCaptor<String> rolesEnumArgumentCaptor;

    @BeforeEach
    void SetUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    class createUsers {

        userRequestDTO admin = new userRequestDTO("admin", "admin123", Set.of(RolesEnum.ADMIN));
        userRequestDTO user = new userRequestDTO("user", "user123", Set.of(RolesEnum.USER));
        userRequestDTO tecnical = new userRequestDTO("tecnical", "123", Set.of(RolesEnum.TECNICAL));

        @Test
        @DisplayName("should create an admin with success")
        void shouldCreateAnAdminWithSuccess() {

            doReturn(Optional.of(RolesEnum.ADMIN.toRole())).when(rolesRepository).findByDescricao(rolesEnumArgumentCaptor.capture());

            doReturn("encoded-password").when(passwordEncoder).encode("admin123");

            Users entity = admin.toEntity(Set.of(RolesEnum.ADMIN.toRole()));
            entity.setPassword("encoded-password");

            doReturn(entity).when(usersRepository).save(usersArgumentCaptor.capture());

            Users user1 = service.createUser(admin);

            // Compare by description
            Set<String> collect = usersArgumentCaptor.getValue().getRoles().stream().map(Roles::getDescricao).collect(Collectors.toSet());

            assertEquals(Set.of(RolesEnum.ADMIN.getName()), collect);
            assertEquals(user1.getUsername(), usersArgumentCaptor.getValue().getUsername());
            assertEquals("encoded-password", usersArgumentCaptor.getValue().getPassword());


            verify(rolesRepository, times(1)).findByDescricao(rolesEnumArgumentCaptor.getValue());
            verify(usersRepository, times(1)).save(usersArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("should create an user with success")
        void shouldCreateAUserWithSuccess() {

            doReturn(Optional.of(RolesEnum.USER.toRole())).when(rolesRepository).findByDescricao(rolesEnumArgumentCaptor.capture());
            doReturn("encoded-password").when(passwordEncoder).encode("user123");


            Users entity = user.toEntity(Set.of(RolesEnum.USER.toRole()));
            entity.setPassword("encoded-password");

            doReturn(entity).when(usersRepository).save(usersArgumentCaptor.capture());

            Users user1 = service.createUser(user);

            // Compare by description
            Set<String> collect = usersArgumentCaptor.getValue().getRoles().stream().map(Roles::getDescricao).collect(Collectors.toSet());
            assertEquals(Set.of(RolesEnum.USER.getName()), collect);

            assertEquals(user1.getUsername(), usersArgumentCaptor.getValue().getUsername());
            assertEquals(user1.getPassword(), usersArgumentCaptor.getValue().getPassword());

            assertEquals(RolesEnum.USER.getName(), rolesEnumArgumentCaptor.getValue());

            verify(rolesRepository, times(1)).findByDescricao(rolesEnumArgumentCaptor.getValue());
            verify(usersRepository, times(1)).save(usersArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("should create a tecnical with success")
        void shouldCreateATecnicalWithSuccess() {

            doReturn(Optional.of(RolesEnum.TECNICAL.toRole())).when(rolesRepository).findByDescricao(rolesEnumArgumentCaptor.capture());

            doReturn("encoded-password").when(passwordEncoder).encode("123");

            Users entity = tecnical.toEntity(Set.of(RolesEnum.TECNICAL.toRole()));
            entity.setPassword("encoded-password");

            doReturn(entity).when(usersRepository).save(usersArgumentCaptor.capture());

            Users user1 = service.createUser(tecnical);

            // Compare by description
            Set<String> collect = usersArgumentCaptor.getValue().getRoles().stream().map(Roles::getDescricao).collect(Collectors.toSet());
            assertEquals(Set.of(RolesEnum.TECNICAL.getName()), collect);

            assertEquals(user1.getUsername(), usersArgumentCaptor.getValue().getUsername());
            assertEquals(user1.getPassword(), usersArgumentCaptor.getValue().getPassword());

            assertEquals(RolesEnum.TECNICAL.getName(), rolesEnumArgumentCaptor.getValue());

            verify(rolesRepository, times(1)).findByDescricao(rolesEnumArgumentCaptor.getValue());
            verify(usersRepository, times(1)).save(usersArgumentCaptor.getValue());
        }


        @Test
        @DisplayName("should not create an admin when Description not found")
        void shouldNotCreateAnAdminWhenDescriptionNotFound(){
            doReturn(Optional.empty()).when(rolesRepository).findByDescricao(rolesEnumArgumentCaptor.capture());

            RuntimeException roleNaoEncontrada = assertThrows(RuntimeException.class, () -> {
                service.createUser(admin);
            });
            assertEquals("Role não encontrada",roleNaoEncontrada.getMessage());

            verify(rolesRepository, times(1)).findByDescricao(rolesEnumArgumentCaptor.getValue());
            verify(usersRepository, never()).save(any());
            verifyNoMoreInteractions(rolesRepository, usersRepository);
        }

        @Test
        @DisplayName("should create an user with more than one role with success")
        void shouldCreateAnUserWithMoreThanOneRoleWithSuccess(){

            Set<RolesEnum> rolesDto = Set.of(RolesEnum.ADMIN, RolesEnum.TECNICAL);
            userRequestDTO userdto = new userRequestDTO("Gab", "123", rolesDto);

            Roles adminRole = RolesEnum.ADMIN.toRole();
            Roles tecnicalRole = RolesEnum.TECNICAL.toRole();

            doReturn(Optional.of(adminRole)).when(rolesRepository).findByDescricao(RolesEnum.ADMIN.getName());
            doReturn(Optional.of(tecnicalRole)).when(rolesRepository).findByDescricao(RolesEnum.TECNICAL.getName());


            service.createUser(userdto);

            verify(rolesRepository, times(2)).findByDescricao(rolesEnumArgumentCaptor.capture());
            verify(usersRepository, times(1)).save(usersArgumentCaptor.capture());

            List<String> capturados = rolesEnumArgumentCaptor.getAllValues();

            Users savedUser = usersArgumentCaptor.getValue();

            Set<String> rolesSalvas = savedUser.getRoles().stream()
                    .map(Roles::getDescricao)
                    .collect(Collectors.toSet());

            assertTrue(rolesSalvas.containsAll(Set.of("admin", "tecnical")));
            assertTrue(capturados.containsAll(Set.of(RolesEnum.ADMIN.getName(), RolesEnum.TECNICAL.getName())));
        }
    }


}
