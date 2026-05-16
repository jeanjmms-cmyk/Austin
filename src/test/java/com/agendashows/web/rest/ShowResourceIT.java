package com.agendashows.web.rest;

import static com.agendashows.domain.ShowAsserts.*;
import static com.agendashows.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agendashows.IntegrationTest;
import com.agendashows.domain.Cantor;
import com.agendashows.domain.Show;
import com.agendashows.domain.enumeration.StatusShow;
import com.agendashows.repository.ShowRepository;
import com.agendashows.service.ShowService;
import com.agendashows.service.dto.ShowDTO;
import com.agendashows.service.mapper.ShowMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShowResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShowResourceIT {

    private static final String DEFAULT_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_SHOW = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_SHOW = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_SHOW = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_HORARIO_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORARIO_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    private static final StatusShow DEFAULT_STATUS = StatusShow.AGENDADO;
    private static final StatusShow UPDATED_STATUS = StatusShow.CONFIRMADO;

    private static final String ENTITY_API_URL = "/api/shows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShowRepository showRepository;

    @Mock
    private ShowRepository showRepositoryMock;

    @Autowired
    private ShowMapper showMapper;

    @Mock
    private ShowService showServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShowMockMvc;

    private Show show;

    private Show insertedShow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Show createEntity(EntityManager em) {
        Show show = new Show()
            .local(DEFAULT_LOCAL)
            .dataShow(DEFAULT_DATA_SHOW)
            .horarioInicio(DEFAULT_HORARIO_INICIO)
            .observacoes(DEFAULT_OBSERVACOES)
            .status(DEFAULT_STATUS);
        // Add required entity
        Cantor cantor;
        if (TestUtil.findAll(em, Cantor.class).isEmpty()) {
            cantor = CantorResourceIT.createEntity();
            em.persist(cantor);
            em.flush();
        } else {
            cantor = TestUtil.findAll(em, Cantor.class).get(0);
        }
        show.setCantor(cantor);
        return show;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Show createUpdatedEntity(EntityManager em) {
        Show updatedShow = new Show()
            .local(UPDATED_LOCAL)
            .dataShow(UPDATED_DATA_SHOW)
            .horarioInicio(UPDATED_HORARIO_INICIO)
            .observacoes(UPDATED_OBSERVACOES)
            .status(UPDATED_STATUS);
        // Add required entity
        Cantor cantor;
        if (TestUtil.findAll(em, Cantor.class).isEmpty()) {
            cantor = CantorResourceIT.createUpdatedEntity();
            em.persist(cantor);
            em.flush();
        } else {
            cantor = TestUtil.findAll(em, Cantor.class).get(0);
        }
        updatedShow.setCantor(cantor);
        return updatedShow;
    }

    @BeforeEach
    void initTest() {
        show = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedShow != null) {
            showRepository.delete(insertedShow);
            insertedShow = null;
        }
    }

    @Test
    @Transactional
    void createShow() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);
        var returnedShowDTO = om.readValue(
            restShowMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShowDTO.class
        );

        // Validate the Show in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShow = showMapper.toEntity(returnedShowDTO);
        assertShowUpdatableFieldsEquals(returnedShow, getPersistedShow(returnedShow));

        insertedShow = returnedShow;
    }

    @Test
    @Transactional
    void createShowWithExistingId() throws Exception {
        // Create the Show with an existing ID
        show.setId(1L);
        ShowDTO showDTO = showMapper.toDto(show);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLocalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        show.setLocal(null);

        // Create the Show, which fails.
        ShowDTO showDTO = showMapper.toDto(show);

        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataShowIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        show.setDataShow(null);

        // Create the Show, which fails.
        ShowDTO showDTO = showMapper.toDto(show);

        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHorarioInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        show.setHorarioInicio(null);

        // Create the Show, which fails.
        ShowDTO showDTO = showMapper.toDto(show);

        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        show.setStatus(null);

        // Create the Show, which fails.
        ShowDTO showDTO = showMapper.toDto(show);

        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShows() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(show.getId().intValue())))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].dataShow").value(hasItem(DEFAULT_DATA_SHOW.toString())))
            .andExpect(jsonPath("$.[*].horarioInicio").value(hasItem(DEFAULT_HORARIO_INICIO.toString())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShowsWithEagerRelationshipsIsEnabled() throws Exception {
        when(showServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(showServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShowsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(showServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(showRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShow() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get the show
        restShowMockMvc
            .perform(get(ENTITY_API_URL_ID, show.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(show.getId().intValue()))
            .andExpect(jsonPath("$.local").value(DEFAULT_LOCAL))
            .andExpect(jsonPath("$.dataShow").value(DEFAULT_DATA_SHOW.toString()))
            .andExpect(jsonPath("$.horarioInicio").value(DEFAULT_HORARIO_INICIO.toString()))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getShowsByIdFiltering() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        Long id = show.getId();

        defaultShowFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShowFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShowFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShowsByLocalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where local equals to
        defaultShowFiltering("local.equals=" + DEFAULT_LOCAL, "local.equals=" + UPDATED_LOCAL);
    }

    @Test
    @Transactional
    void getAllShowsByLocalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where local in
        defaultShowFiltering("local.in=" + DEFAULT_LOCAL + "," + UPDATED_LOCAL, "local.in=" + UPDATED_LOCAL);
    }

    @Test
    @Transactional
    void getAllShowsByLocalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where local is not null
        defaultShowFiltering("local.specified=true", "local.specified=false");
    }

    @Test
    @Transactional
    void getAllShowsByLocalContainsSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where local contains
        defaultShowFiltering("local.contains=" + DEFAULT_LOCAL, "local.contains=" + UPDATED_LOCAL);
    }

    @Test
    @Transactional
    void getAllShowsByLocalNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where local does not contain
        defaultShowFiltering("local.doesNotContain=" + UPDATED_LOCAL, "local.doesNotContain=" + DEFAULT_LOCAL);
    }

    @Test
    @Transactional
    void getAllShowsByDataShowIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where dataShow equals to
        defaultShowFiltering("dataShow.equals=" + DEFAULT_DATA_SHOW, "dataShow.equals=" + UPDATED_DATA_SHOW);
    }

    @Test
    @Transactional
    void getAllShowsByDataShowIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where dataShow in
        defaultShowFiltering("dataShow.in=" + DEFAULT_DATA_SHOW + "," + UPDATED_DATA_SHOW, "dataShow.in=" + UPDATED_DATA_SHOW);
    }

    @Test
    @Transactional
    void getAllShowsByDataShowIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where dataShow is not null
        defaultShowFiltering("dataShow.specified=true", "dataShow.specified=false");
    }

    @Test
    @Transactional
    void getAllShowsByDataShowIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where dataShow is greater than or equal to
        defaultShowFiltering("dataShow.greaterThanOrEqual=" + DEFAULT_DATA_SHOW, "dataShow.greaterThanOrEqual=" + UPDATED_DATA_SHOW);
    }

    @Test
    @Transactional
    void getAllShowsByDataShowIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where dataShow is less than or equal to
        defaultShowFiltering("dataShow.lessThanOrEqual=" + DEFAULT_DATA_SHOW, "dataShow.lessThanOrEqual=" + SMALLER_DATA_SHOW);
    }

    @Test
    @Transactional
    void getAllShowsByDataShowIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where dataShow is less than
        defaultShowFiltering("dataShow.lessThan=" + UPDATED_DATA_SHOW, "dataShow.lessThan=" + DEFAULT_DATA_SHOW);
    }

    @Test
    @Transactional
    void getAllShowsByDataShowIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where dataShow is greater than
        defaultShowFiltering("dataShow.greaterThan=" + SMALLER_DATA_SHOW, "dataShow.greaterThan=" + DEFAULT_DATA_SHOW);
    }

    @Test
    @Transactional
    void getAllShowsByHorarioInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where horarioInicio equals to
        defaultShowFiltering("horarioInicio.equals=" + DEFAULT_HORARIO_INICIO, "horarioInicio.equals=" + UPDATED_HORARIO_INICIO);
    }

    @Test
    @Transactional
    void getAllShowsByHorarioInicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where horarioInicio in
        defaultShowFiltering(
            "horarioInicio.in=" + DEFAULT_HORARIO_INICIO + "," + UPDATED_HORARIO_INICIO,
            "horarioInicio.in=" + UPDATED_HORARIO_INICIO
        );
    }

    @Test
    @Transactional
    void getAllShowsByHorarioInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where horarioInicio is not null
        defaultShowFiltering("horarioInicio.specified=true", "horarioInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllShowsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where status equals to
        defaultShowFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllShowsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where status in
        defaultShowFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllShowsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        // Get all the showList where status is not null
        defaultShowFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllShowsByCantorIsEqualToSomething() throws Exception {
        Cantor cantor;
        if (TestUtil.findAll(em, Cantor.class).isEmpty()) {
            showRepository.saveAndFlush(show);
            cantor = CantorResourceIT.createEntity();
        } else {
            cantor = TestUtil.findAll(em, Cantor.class).get(0);
        }
        em.persist(cantor);
        em.flush();
        show.setCantor(cantor);
        showRepository.saveAndFlush(show);
        Long cantorId = cantor.getId();
        // Get all the showList where cantor equals to cantorId
        defaultShowShouldBeFound("cantorId.equals=" + cantorId);

        // Get all the showList where cantor equals to (cantorId + 1)
        defaultShowShouldNotBeFound("cantorId.equals=" + (cantorId + 1));
    }

    private void defaultShowFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShowShouldBeFound(shouldBeFound);
        defaultShowShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShowShouldBeFound(String filter) throws Exception {
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(show.getId().intValue())))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].dataShow").value(hasItem(DEFAULT_DATA_SHOW.toString())))
            .andExpect(jsonPath("$.[*].horarioInicio").value(hasItem(DEFAULT_HORARIO_INICIO.toString())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShowShouldNotBeFound(String filter) throws Exception {
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShow() throws Exception {
        // Get the show
        restShowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShow() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the show
        Show updatedShow = showRepository.findById(show.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShow are not directly saved in db
        em.detach(updatedShow);
        updatedShow
            .local(UPDATED_LOCAL)
            .dataShow(UPDATED_DATA_SHOW)
            .horarioInicio(UPDATED_HORARIO_INICIO)
            .observacoes(UPDATED_OBSERVACOES)
            .status(UPDATED_STATUS);
        ShowDTO showDTO = showMapper.toDto(updatedShow);

        restShowMockMvc
            .perform(put(ENTITY_API_URL_ID, showDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showDTO)))
            .andExpect(status().isOk());

        // Validate the Show in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShowToMatchAllProperties(updatedShow);
    }

    @Test
    @Transactional
    void putNonExistingShow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        show.setId(longCount.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(put(ENTITY_API_URL_ID, showDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        show.setId(longCount.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(showDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        show.setId(longCount.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(showDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Show in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShowWithPatch() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the show using partial update
        Show partialUpdatedShow = new Show();
        partialUpdatedShow.setId(show.getId());

        partialUpdatedShow.dataShow(UPDATED_DATA_SHOW).status(UPDATED_STATUS);

        restShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShow.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShow))
            )
            .andExpect(status().isOk());

        // Validate the Show in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShowUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedShow, show), getPersistedShow(show));
    }

    @Test
    @Transactional
    void fullUpdateShowWithPatch() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the show using partial update
        Show partialUpdatedShow = new Show();
        partialUpdatedShow.setId(show.getId());

        partialUpdatedShow
            .local(UPDATED_LOCAL)
            .dataShow(UPDATED_DATA_SHOW)
            .horarioInicio(UPDATED_HORARIO_INICIO)
            .observacoes(UPDATED_OBSERVACOES)
            .status(UPDATED_STATUS);

        restShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShow.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShow))
            )
            .andExpect(status().isOk());

        // Validate the Show in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShowUpdatableFieldsEquals(partialUpdatedShow, getPersistedShow(partialUpdatedShow));
    }

    @Test
    @Transactional
    void patchNonExistingShow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        show.setId(longCount.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, showDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(showDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        show.setId(longCount.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(showDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        show.setId(longCount.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(showDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Show in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShow() throws Exception {
        // Initialize the database
        insertedShow = showRepository.saveAndFlush(show);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the show
        restShowMockMvc
            .perform(delete(ENTITY_API_URL_ID, show.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return showRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Show getPersistedShow(Show show) {
        return showRepository.findById(show.getId()).orElseThrow();
    }

    protected void assertPersistedShowToMatchAllProperties(Show expectedShow) {
        assertShowAllPropertiesEquals(expectedShow, getPersistedShow(expectedShow));
    }

    protected void assertPersistedShowToMatchUpdatableProperties(Show expectedShow) {
        assertShowAllUpdatablePropertiesEquals(expectedShow, getPersistedShow(expectedShow));
    }
}
