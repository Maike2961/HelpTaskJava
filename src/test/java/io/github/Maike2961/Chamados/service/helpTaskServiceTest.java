package io.github.Maike2961.Chamados.service;

import io.github.Maike2961.Chamados.Enums.RolesEnum;
import io.github.Maike2961.Chamados.Enums.StatusEnum;
import io.github.Maike2961.Chamados.repository.UsersRepository;
import io.github.Maike2961.Chamados.repository.helpTaskRepository;
import io.github.Maike2961.Chamados.controller.dto.taskRequestDTO;
import io.github.Maike2961.Chamados.controller.dto.taskResponseDTO;
import io.github.Maike2961.Chamados.controller.dto.updateRequestDTO;
import io.github.Maike2961.Chamados.exception.UsernameNaoEncontrado;
import io.github.Maike2961.Chamados.models.Users;
import io.github.Maike2961.Chamados.models.helpTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(MockitoExtension.class)
class helpTaskServiceTest {

    @Mock
    private helpTaskRepository repository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private helpTaskService service;

    @Captor
    private ArgumentCaptor<helpTask> helpTaskCaptor;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    class createHelpTask {

        @Test
        @DisplayName("should create a help task with success")
        void shouldCreateAHelpTaskWithSuccess() {

            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            Mockito.when(authentication.getName()).thenReturn("teste");

            var user = createUser();

            var task = new taskRequestDTO("Preciso de ajuda com login", "teste");

            helpTask entity = task.toEntity();

            doReturn(Optional.of(user)).when(usersRepository).findByUsername("teste");
            doReturn(entity).when(repository).save(helpTaskCaptor.capture());

            service.createTask(task);

            verify(repository, times(1)).save(helpTaskCaptor.getValue());
            verify(usersRepository, times(1)).findByUsername("teste");

            assertEquals("Preciso de ajuda com login", helpTaskCaptor.getValue().getHelpTitle());

        }

        @Test
        @DisplayName("should not create a help task with success when user id is not found")
        void shouldNotCreateAHelpTaskWithSuccessWhenUserIdIsNotFound(){

            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            Mockito.when(authentication.getName()).thenReturn("teste");

            var task = new taskRequestDTO("Preciso de ajuda com login","teste");

            doReturn(Optional.empty()).when(usersRepository).findByUsername("teste");

            assertThrows(UsernameNaoEncontrado.class, ()->{
                service.createTask(task);
            });

            verify(usersRepository, times(1)).findByUsername("teste");
            verify(repository, never()).save(any());

        }

    }

    @Nested
    class listAllHelpTask{

        @Test
        @DisplayName("Should Get All Help Tasks With Success")
        void shouldGetAllHelpTasksWithSuccess(){
            var task = new taskRequestDTO("Teste", "Teste descricao");
            var task2 = new taskRequestDTO("Teste 2", "Teste descricao 2");


            doReturn(List.of(task.toEntity(), task2.toEntity())).when(repository).findAll();

            List<helpTask> helpTasks = service.listAllTasks();

            assertEquals(2, helpTasks.size());
            assertFalse(helpTasks.isEmpty());
            verifyNoMoreInteractions(repository);
            verify(repository, times(1)).findAll();
        }


        @Test
        @DisplayName("Should Not Return All Help Task When List Is Empty")
        void shouldNotReturnAllHelpTaskWhenListIsEmpty(){

            doReturn(List.of()).when(repository).findAll();

            List<helpTask> helpTasks = service.listAllTasks();

            assertEquals(0, helpTasks.size());
            assertTrue(helpTasks.isEmpty());
            verify(repository, times(1)).findAll();
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("should only return a list when is an user or admin")
        void shouldOnlyReturnAListWhenIsAnUserOrAdmin(){

            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            var user = createUser();

            var task = new taskRequestDTO("Teste", "Teste descricao");
            helpTask entity = task.toEntity();
            entity.setUsers(user);

            var task2 = new taskRequestDTO("Teste 2", "Teste descricao 2");
            helpTask entity2 = task2.toEntity();
            entity2.setUsers(user);


            Mockito.when(authentication.getName()).thenReturn("Teste");
            doReturn(Optional.of(user)).when(usersRepository).findByUsername(user.getUsername());
            doReturn(List.of(entity, entity2)).when(repository).findByUsers(user);

            List<taskResponseDTO> taskResponseDTOS = service.listUserOrAdminTasks();


            assertEquals(2, taskResponseDTOS.size());
            assertTrue(user.getRoles().stream().anyMatch(i -> i.getDescricao().equals("user")));
            verifyNoMoreInteractions(repository);
        }


    }

    @Nested
    class updateHelpTask{

        @Test
        @DisplayName("should update a help task when id found")
        void shouldUpdateAHelpTaskWhenIdFound(){

            var user = createUser();
            var task = new taskRequestDTO("Teste", "Teste descricao");
            helpTask entity = task.toEntity();
            entity.setUsers(user);
            entity.setStatus(StatusEnum.PENDENTE.toStatus());

            doReturn(Optional.of(entity)).when(repository).findById(1L);
            doReturn(entity).when(repository).save(helpTaskCaptor.capture());

            var update  = new updateRequestDTO(StatusEnum.CONCLUIDO);

            taskResponseDTO taskResponseDTO = service.updateTask(1L, update);

            assertEquals(helpTaskCaptor.getValue().getStatus().getDescricao(), taskResponseDTO.status().getDescricao());
            assertEquals(helpTaskCaptor.getValue().getHelpTitle(), taskResponseDTO.title());

            verify(repository, times(1)).findById(1L);
            verify(repository, times(1)).save(helpTaskCaptor.getValue());
        }
    }

    private Users createUser(){
        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setUsername("Teste");
        user.setPassword("Teste123");
        user.setHelpTaskList(List.of());
        user.setRoles(Set.of(RolesEnum.USER.toRole()));
        return user;
    }

}