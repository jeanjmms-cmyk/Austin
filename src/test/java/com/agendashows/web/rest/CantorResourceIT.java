package com.agendashows.web.rest;

import static com.agendashows.domain.CantorAsserts.*;
import static com.agendashows.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agendashows.IntegrationTest;
import com.agendashows.domain.Cantor;
import com.agendashows.repository.CantorRepository;
import com.agendashows.service.dto.CantorDTO;
import com.agendashows.service.mapper.CantorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CantorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CantorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_GENERO_MUSICAL = "AAAAAAAAAA";
    private static final String UPDATED_GENERO_MUSICAL = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_FOTO_PERFIL = "AAAAAAAAAA";
    private static final String UPDATED_FOTO_PERFIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/cantors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CantorRepository cantorRepository;

    @Autowired
    private CantorMapper cantorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCantorMockMvc;

    private Cantor cantor;

    private Cantor insertedCantor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cantor createEntity() {
        return new Cantor()
            .nome(DEFAULT_NOME)
            .generoMusical(DEFAULT_GENERO_MUSICAL)
            .bio(DEFAULT_BIO)
            .fotoPerfil(DEFAULT_FOTO_PERFIL)
            .ativo(DEFAULT_ATIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cantor createUpdatedEntity() {
        return new Cantor()
            .nome(UPDATED_NOME)
            .generoMusical(UPDATED_GENERO_MUSICAL)
            .bio(UPDATED_BIO)
            .fotoPerfil(UPDATED_FOTO_PERFIL)
            .ativo(UPDATED_ATIVO);
    }

    @BeforeEach
    void initTest() {
        cantor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCantor != null) {
            cantorRepository.delete(insertedCantor);
            insertedCantor = null;
        }
    }

    @Test
    @Transactional
    void createCantor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cantor
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);
        var returnedCantorDTO = om.readValue(
            restCantorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cantorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CantorDTO.class
        );

        // Validate the Cantor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCantor = cantorMapper.toEntity(returnedCantorDTO);
        assertCantorUpdatableFieldsEquals(returnedCantor, getPersistedCantor(returnedCantor));

        insertedCantor = returnedCantor;
    }

    @Test
    @Transactional
    void createCantorWithExistingId() throws Exception {
        // Create the Cantor with an existing ID
        cantor.setId(1L);
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCantorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cantorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cantor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cantor.setNome(null);

        // Create the Cantor, which fails.
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);

        restCantorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cantorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cantor.setAtivo(null);

        // Create the Cantor, which fails.
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);

        restCantorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cantorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCantors() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList
        restCantorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cantor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].generoMusical").value(hasItem(DEFAULT_GENERO_MUSICAL)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].fotoPerfil").value(hasItem(DEFAULT_FOTO_PERFIL)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @Test
    @Transactional
    void getCantor() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get the cantor
        restCantorMockMvc
            .perform(get(ENTITY_API_URL_ID, cantor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cantor.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.generoMusical").value(DEFAULT_GENERO_MUSICAL))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO))
            .andExpect(jsonPath("$.fotoPerfil").value(DEFAULT_FOTO_PERFIL))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getCantorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        Long id = cantor.getId();

        defaultCantorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCantorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCantorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCantorsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where nome equals to
        defaultCantorFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCantorsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where nome in
        defaultCantorFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCantorsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where nome is not null
        defaultCantorFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllCantorsByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where nome contains
        defaultCantorFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCantorsByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where nome does not contain
        defaultCantorFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllCantorsByGeneroMusicalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where generoMusical equals to
        defaultCantorFiltering("generoMusical.equals=" + DEFAULT_GENERO_MUSICAL, "generoMusical.equals=" + UPDATED_GENERO_MUSICAL);
    }

    @Test
    @Transactional
    void getAllCantorsByGeneroMusicalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where generoMusical in
        defaultCantorFiltering(
            "generoMusical.in=" + DEFAULT_GENERO_MUSICAL + "," + UPDATED_GENERO_MUSICAL,
            "generoMusical.in=" + UPDATED_GENERO_MUSICAL
        );
    }

    @Test
    @Transactional
    void getAllCantorsByGeneroMusicalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where generoMusical is not null
        defaultCantorFiltering("generoMusical.specified=true", "generoMusical.specified=false");
    }

    @Test
    @Transactional
    void getAllCantorsByGeneroMusicalContainsSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where generoMusical contains
        defaultCantorFiltering("generoMusical.contains=" + DEFAULT_GENERO_MUSICAL, "generoMusical.contains=" + UPDATED_GENERO_MUSICAL);
    }

    @Test
    @Transactional
    void getAllCantorsByGeneroMusicalNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where generoMusical does not contain
        defaultCantorFiltering(
            "generoMusical.doesNotContain=" + UPDATED_GENERO_MUSICAL,
            "generoMusical.doesNotContain=" + DEFAULT_GENERO_MUSICAL
        );
    }

    @Test
    @Transactional
    void getAllCantorsByFotoPerfilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where fotoPerfil equals to
        defaultCantorFiltering("fotoPerfil.equals=" + DEFAULT_FOTO_PERFIL, "fotoPerfil.equals=" + UPDATED_FOTO_PERFIL);
    }

    @Test
    @Transactional
    void getAllCantorsByFotoPerfilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where fotoPerfil in
        defaultCantorFiltering("fotoPerfil.in=" + DEFAULT_FOTO_PERFIL + "," + UPDATED_FOTO_PERFIL, "fotoPerfil.in=" + UPDATED_FOTO_PERFIL);
    }

    @Test
    @Transactional
    void getAllCantorsByFotoPerfilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where fotoPerfil is not null
        defaultCantorFiltering("fotoPerfil.specified=true", "fotoPerfil.specified=false");
    }

    @Test
    @Transactional
    void getAllCantorsByFotoPerfilContainsSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where fotoPerfil contains
        defaultCantorFiltering("fotoPerfil.contains=" + DEFAULT_FOTO_PERFIL, "fotoPerfil.contains=" + UPDATED_FOTO_PERFIL);
    }

    @Test
    @Transactional
    void getAllCantorsByFotoPerfilNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where fotoPerfil does not contain
        defaultCantorFiltering("fotoPerfil.doesNotContain=" + UPDATED_FOTO_PERFIL, "fotoPerfil.doesNotContain=" + DEFAULT_FOTO_PERFIL);
    }

    @Test
    @Transactional
    void getAllCantorsByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where ativo equals to
        defaultCantorFiltering("ativo.equals=" + DEFAULT_ATIVO, "ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllCantorsByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where ativo in
        defaultCantorFiltering("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO, "ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllCantorsByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        // Get all the cantorList where ativo is not null
        defaultCantorFiltering("ativo.specified=true", "ativo.specified=false");
    }

    private void defaultCantorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCantorShouldBeFound(shouldBeFound);
        defaultCantorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCantorShouldBeFound(String filter) throws Exception {
        restCantorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cantor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].generoMusical").value(hasItem(DEFAULT_GENERO_MUSICAL)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].fotoPerfil").value(hasItem(DEFAULT_FOTO_PERFIL)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));

        // Check, that the count call also returns 1
        restCantorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCantorShouldNotBeFound(String filter) throws Exception {
        restCantorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCantorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCantor() throws Exception {
        // Get the cantor
        restCantorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCantor() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cantor
        Cantor updatedCantor = cantorRepository.findById(cantor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCantor are not directly saved in db
        em.detach(updatedCantor);
        updatedCantor
            .nome(UPDATED_NOME)
            .generoMusical(UPDATED_GENERO_MUSICAL)
            .bio(UPDATED_BIO)
            .fotoPerfil(UPDATED_FOTO_PERFIL)
            .ativo(UPDATED_ATIVO);
        CantorDTO cantorDTO = cantorMapper.toDto(updatedCantor);

        restCantorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cantorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cantorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cantor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCantorToMatchAllProperties(updatedCantor);
    }

    @Test
    @Transactional
    void putNonExistingCantor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cantor.setId(longCount.incrementAndGet());

        // Create the Cantor
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCantorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cantorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cantorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cantor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCantor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cantor.setId(longCount.incrementAndGet());

        // Create the Cantor
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCantorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cantorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cantor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCantor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cantor.setId(longCount.incrementAndGet());

        // Create the Cantor
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCantorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cantorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cantor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCantorWithPatch() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cantor using partial update
        Cantor partialUpdatedCantor = new Cantor();
        partialUpdatedCantor.setId(cantor.getId());

        restCantorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCantor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCantor))
            )
            .andExpect(status().isOk());

        // Validate the Cantor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCantorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCantor, cantor), getPersistedCantor(cantor));
    }

    @Test
    @Transactional
    void fullUpdateCantorWithPatch() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cantor using partial update
        Cantor partialUpdatedCantor = new Cantor();
        partialUpdatedCantor.setId(cantor.getId());

        partialUpdatedCantor
            .nome(UPDATED_NOME)
            .generoMusical(UPDATED_GENERO_MUSICAL)
            .bio(UPDATED_BIO)
            .fotoPerfil(UPDATED_FOTO_PERFIL)
            .ativo(UPDATED_ATIVO);

        restCantorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCantor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCantor))
            )
            .andExpect(status().isOk());

        // Validate the Cantor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCantorUpdatableFieldsEquals(partialUpdatedCantor, getPersistedCantor(partialUpdatedCantor));
    }

    @Test
    @Transactional
    void patchNonExistingCantor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cantor.setId(longCount.incrementAndGet());

        // Create the Cantor
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCantorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cantorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cantorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cantor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCantor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cantor.setId(longCount.incrementAndGet());

        // Create the Cantor
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCantorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cantorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cantor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCantor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cantor.setId(longCount.incrementAndGet());

        // Create the Cantor
        CantorDTO cantorDTO = cantorMapper.toDto(cantor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCantorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cantorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cantor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCantor() throws Exception {
        // Initialize the database
        insertedCantor = cantorRepository.saveAndFlush(cantor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cantor
        restCantorMockMvc
            .perform(delete(ENTITY_API_URL_ID, cantor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cantorRepository.count();
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

    protected Cantor getPersistedCantor(Cantor cantor) {
        return cantorRepository.findById(cantor.getId()).orElseThrow();
    }

    protected void assertPersistedCantorToMatchAllProperties(Cantor expectedCantor) {
        assertCantorAllPropertiesEquals(expectedCantor, getPersistedCantor(expectedCantor));
    }

    protected void assertPersistedCantorToMatchUpdatableProperties(Cantor expectedCantor) {
        assertCantorAllUpdatablePropertiesEquals(expectedCantor, getPersistedCantor(expectedCantor));
    }
}
