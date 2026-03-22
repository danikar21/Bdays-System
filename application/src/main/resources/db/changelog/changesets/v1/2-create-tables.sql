CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);


CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT REFERENCES teams(id) ON DELETE RESTRICT,
    email VARCHAR(255) NOT NULL UNIQUE,
    image_url TEXT,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL DEFAULT 'GUEST',
    first_name VARCHAR(100),
    patronymic VARCHAR(100),
    last_name VARCHAR(100),
    birth_date DATE,
    position VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    active BOOLEAN DEFAULT TRUE
);

CREATE INDEX idx_accounts_team_id ON accounts(team_id);


CREATE TABLE subscription_team (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    target_team_id BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (follower_id, target_team_id)
);

CREATE INDEX idx_sub_team_follower ON subscription_team(follower_id);
CREATE INDEX idx_sub_team_target ON subscription_team(target_team_id);


CREATE TABLE subscription_user (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    target_employee_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (follower_id, target_employee_id)
);

CREATE INDEX idx_sub_us_follower ON subscription_user(follower_id);
CREATE INDEX idx_sub_us_target ON subscription_user(target_employee_id);


CREATE TABLE wish_item (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    link TEXT,
    image_url TEXT,
    price DECIMAL(10,2),
    is_chosen BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE INDEX idx_wish_item_account ON wish_item(account_id);

CREATE TABLE wish_votes (
    id BIGSERIAL PRIMARY KEY,
    wish_item_id BIGINT NOT NULL REFERENCES wish_item(id) ON DELETE CASCADE,
    account_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (wish_item_id, account_id)
);

CREATE INDEX idx_wish_votes_wish_item ON wish_votes(wish_item_id);
CREATE INDEX idx_wish_votes_account ON wish_votes(account_id);


CREATE TABLE gift_discussions (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    author_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_gift_discussions_owner_created_id ON gift_discussions(owner_id, created_at DESC, id DESC);
CREATE INDEX idx_gift_discussions_author ON gift_discussions(author_id);


CREATE TABLE payment_collections (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
    wish_item_id BIGINT REFERENCES wish_item(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    collected_amount DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (collected_amount >= 0),
    end_date DATE NOT NULL,
    status collection_status NOT NULL DEFAULT 'ACTIVE',
    external_id VARCHAR(255) UNIQUE,
    link TEXT,
    description TEXT
);

CREATE INDEX idx_payment_collections_account ON payment_collections(account_id);
CREATE INDEX idx_payment_collections_wish_item ON payment_collections(wish_item_id);
CREATE INDEX idx_payment_collections_status ON payment_collections(status);
