# AI-Powered E-Commerce Backend - Technical Summary

## 1. PROJECT OVERVIEW

### Problem Statement
This project addresses the need for a **complete, production-ready e-commerce backend** that goes beyond basic CRUD operations. It solves real-world challenges in online retail including:
- Complex order management with status tracking
- Return and refund processing with policy enforcement
- Inventory management with stock validation
- AI-powered product description generation
- Role-based access control for different user types
- Secure authentication and authorization
- Comprehensive product catalog management

### Real-World Use Case
**Business Perspective**: This system powers an e-commerce platform where:
- **Customers** browse products, add items to cart, place orders, write reviews, and request returns
- **Product Managers** manage inventory, update product details, upload images, and generate AI descriptions
- **Administrators** manage users, approve returns, assign roles, and oversee system operations

The system handles the complete e-commerce lifecycle from product cataloging to order fulfillment and post-purchase support (returns/refunds).

### Target Users
1. **End Users (ROLE_USER)**: Regular customers shopping, placing orders, writing reviews
2. **Managers (ROLE_MANAGER)**: Product managers handling inventory, products, categories, and AI features
3. **Administrators (ROLE_ADMIN)**: System administrators managing users, roles, return approvals, and policies

---

## 2. TECH STACK

### Core Framework
- **Java 21**: Modern Java with records, pattern matching, and enhanced performance
- **Spring Boot 3.5.9**: Latest stable version with enhanced security and performance
- **Maven**: Build automation and dependency management

### Database & Persistence
- **MySQL 8.0+**: Relational database for data persistence
- **Spring Data JPA**: Repository abstraction layer
- **Hibernate**: JPA implementation with automatic schema updates (`ddl-auto: update`)
- **Entity Relationships**: One-to-Many, Many-to-One, One-to-One mappings with lazy loading

### Security & Authentication
- **Spring Security**: Comprehensive security framework
- **JWT (JSON Web Tokens)**: Stateless authentication using `jjwt` library (v0.13.0)
- **BCrypt Password Encoding**: Industry-standard hashing with strength factor 12
- **Role-Based Access Control**: Three-tier permission system (ADMIN, MANAGER, USER)
- **Method-Level Security**: `@PreAuthorize` annotations for fine-grained access control

### API Documentation
- **SpringDoc OpenAPI 3.0**: Interactive API documentation
- **Swagger UI**: Browser-based API testing interface
- **JWT Security Integration**: Token-based authentication in Swagger

### External Integrations
- **Cloudinary 1.36.0**: Cloud-based image storage and CDN for product images
  - Supports JPEG, PNG, WEBP formats
  - Maximum file size: 10MB (configurable)
  - Automatic image optimization and transformation
- **Spring AI 1.0.0**: AI integration framework
  - **Ollama**: Local AI model provider (runs on localhost:11434)
  - Model: `qwen2.5:0.5b` for product description generation
  - RAG (Retrieval Augmented Generation) support with query transformers

### Data Transfer & Validation
- **DTO Pattern**: Separate request/response DTOs for API contracts
- **Jakarta Validation**: Input validation with `@NotNull`, `@NotBlank`, `@Size`, `@Email`, etc.
- **Mapper Classes**: Manual DTO-to-Entity conversion for clean separation

### Performance & Scalability
- **Pagination**: Spring Data `Page<T>` with configurable page size (default: 10, max: 100)
- **Advanced Filtering**: JPA Specifications for complex query building (orders by date, status, amount)
- **Lazy Loading**: JPA fetch strategies to optimize database queries
- **Transaction Management**: `@Transactional` for data consistency

### Containerization
- **Docker**: Production-ready Dockerfile with multi-stage build
- **Java 22 Runtime**: Eclipse Temurin JRE for containerized deployment
- **Environment Variables**: Externalized configuration for secrets and settings

### Development Tools
- **Lombok 1.18.42**: Reduces boilerplate code (getters, setters, builders)
- **Spring Boot DevTools**: Hot reload and development enhancements
- **SLF4J Logging**: Structured logging throughout the application

---

## 3. HIGH-LEVEL ARCHITECTURE

### Layered Architecture Pattern

The system follows a **strict layered architecture** with clear separation of concerns:

#### **Presentation Layer (Controllers)**
- **11 REST Controllers** handling HTTP requests/responses
- Responsibilities:
  - Accept HTTP requests
  - Validate input via Jakarta Validation
  - Delegate to service layer
  - Return DTOs as JSON responses
  - Handle Swagger annotations for documentation

#### **Business Logic Layer (Services)**
- **14 Service Classes** containing all business logic
- Responsibilities:
  - Implement business rules and validations
  - Coordinate between repositories
  - Handle transactions (`@Transactional`)
  - Integrate with external services (Cloudinary, AI)
  - Enforce security via `@PreAuthorize`
  - Map entities to DTOs

#### **Data Access Layer (Repositories)**
- **10 Spring Data JPA Repositories** for database operations
- Responsibilities:
  - Abstract database access
  - Provide query methods (derived queries, custom queries)
  - Handle pagination and sorting
  - Support JPA Specifications for dynamic queries

#### **Entity Layer**
- **11 Entity Classes** representing database tables
- Responsibilities:
  - Define database schema
  - Establish relationships (One-to-Many, Many-to-One, One-to-One)
  - Include validation constraints
  - Track timestamps (createdAt, updatedAt)

### DTO Usage and Rationale

**Why DTOs?**
1. **API Contract Stability**: DTOs provide a stable API interface independent of internal entity changes
2. **Security**: Prevents exposing sensitive entity fields (passwords, internal IDs)
3. **Performance**: Allows fetching only required fields, reducing payload size
4. **Validation**: Separate validation rules for API input vs. database constraints
5. **Versioning**: Easier to evolve API without breaking changes

**DTO Categories:**
- **Request DTOs**: Input validation and data transfer (e.g., `ProductCreateDto`, `LoginRequestDto`)
- **Response DTOs**: Formatted output (e.g., `ProductResponseDto`, `OrderResponseDto`)
- **Utility DTOs**: Helper structures (e.g., `ApiResponseDto`, `ErrorResponseDto`)

### Entity Relationships

**Core Relationships:**
- **User → Cart**: One-to-One (each user has one cart)
- **User → Orders**: One-to-Many (user can have multiple orders)
- **User → Reviews**: One-to-Many (user can write multiple reviews)
- **Product → Category**: Many-to-One (products belong to categories)
- **Product → OrderItems**: One-to-Many (product can be in multiple orders)
- **Product → CartItems**: One-to-Many (product can be in multiple carts)
- **Product → Reviews**: One-to-Many (product can have multiple reviews)
- **Order → OrderItems**: One-to-Many (order contains multiple items)
- **Order → User**: Many-to-One (order belongs to a user)
- **Category → ReturnPolicy**: Many-to-One (category has a return policy)
- **OrderItem → Return**: One-to-One (each return request is for one order item)

**Relationship Strategy:**
- **Cascade Operations**: `CascadeType.ALL` for parent-child relationships (e.g., Order → OrderItems)
- **Orphan Removal**: Automatic cleanup when parent is deleted
- **Lazy Loading**: `FetchType.LAZY` to optimize query performance
- **Bidirectional Mappings**: Entities maintain references to related entities

### Exception Handling Strategy

**Global Exception Handler** (`@RestControllerAdvice`):
- **Centralized Error Handling**: Single point for all exception processing
- **Uniform Error Response**: Standard `ErrorResponseDto` structure:
  ```json
  {
    "message": "Error description",
    "errorCode": "HTTP_STATUS_CODE",
    "timeStamp": "2024-01-01T12:00:00"
  }
  ```
- **Exception Categories**:
  - `ResourceNotFoundException` → 404 Not Found
  - `InvalidInputException` → 400 Bad Request
  - `AccessDeniedException` → 403 Forbidden
  - `DuplicationEntryException` → 400 Bad Request
  - `InSufficientStockException` → 400 Bad Request
  - `AlreadyReviewedException` → 400 Bad Request
  - `ReturnWindowExpirationException` → 400 Bad Request
  - `MethodArgumentNotValidException` → 400 with field-level errors
  - Generic `Exception` → 500 Internal Server Error

**Validation Error Handling:**
- Jakarta Validation errors are caught and returned as field-level error maps
- Each field error includes the field name and validation message

### Security Filter Flow (JWT)

**Request Flow:**
1. **Client Request** → HTTP request with `Authorization: Bearer <token>` header
2. **JWT Filter** (`JwtAuthFilter`) intercepts request:
   - Extracts token from Authorization header
   - Validates token signature and expiration
   - Loads user details from database
   - Creates `Authentication` object with user authorities
   - Sets authentication in `SecurityContext`
3. **Spring Security Filter Chain**:
   - Checks URL patterns against security rules
   - Verifies role requirements (`hasRole('ADMIN')`, `hasAnyRole('ADMIN','MANAGER')`)
   - Allows or denies request
4. **Method-Level Security** (`@PreAuthorize`):
   - Additional checks at service method level
   - Supports SpEL expressions for complex authorization logic
   - Examples: `@auth.canAccessOrder(#orderID)`, `#userId==principal.id`

**Token Generation:**
- Token contains: username (subject), issued at, expiration (24 hours)
- Signed with HMAC-SHA256 using secret key from configuration
- No sensitive data stored in token (stateless design)

---

## 4. AUTHENTICATION & AUTHORIZATION

### JWT Login Flow (Step-by-Step)

1. **User Registration** (`POST /auth/user/register`):
   - Client sends `UserRegisterDto` (username, email, password, name)
   - Password is hashed using BCrypt (strength: 12)
   - User entity is saved with default role `ROLE_USER`
   - Returns `UserDto` (without password)

2. **User Login** (`POST /auth/user/jwt/login`):
   - Client sends `LoginRequestDto` (username, password)
   - `AuthenticationManager` authenticates credentials
   - On success, `JwtAuthUtil` generates JWT token:
     - Subject: username
     - Issued At: current time
     - Expiration: 24 hours from issue
     - Signed with secret key
   - Returns `LoginResponseDto` containing JWT token and username

3. **Subsequent Requests**:
   - Client includes token in header: `Authorization: Bearer <token>`
   - `JwtAuthFilter` validates token on every request
   - If valid, request proceeds; if invalid/expired, returns 401 Unauthorized

### Token Generation and Validation

**Token Generation** (`JwtAuthUtil.generateToken()`):
- Uses `io.jsonwebtoken.Jwts.builder()`
- Sets subject (username), issued at, expiration
- Signs with HMAC-SHA256 secret key
- Returns compact JWT string

**Token Validation** (`JwtAuthFilter.doFilterInternal()`):
- Extracts token from `Authorization` header (removes "Bearer " prefix)
- Parses token to extract username
- Loads `UserDetailsPrincipal` from database
- Validates:
  - Token signature (using secret key)
  - Token expiration (not expired)
  - Username matches loaded user
- If valid, creates `UsernamePasswordAuthenticationToken` and sets in `SecurityContext`

### Role-Based Access Control

**Three Roles:**
1. **ROLE_ADMIN**:
   - Full system access
   - User management (enable/disable, role assignment)
   - Return request approvals/rejections
   - Return policy management
   - Product deletion

2. **ROLE_MANAGER**:
   - Product and category management (CRUD)
   - Inventory management (stock updates)
   - AI description generation
   - Image uploads
   - Price and discount updates

3. **ROLE_USER**:
   - Browse products, search, view categories
   - Cart management
   - Order placement and tracking
   - Review submission
   - Return request creation
   - Profile management

**Access Control Mechanisms:**
- **URL-Level**: `SecurityConfig` defines path-based rules:
  - `/admin/**` → ADMIN only
  - `/inventory/**`, `POST /product`, `POST /category` → ADMIN or MANAGER
  - `/auth/user/**` → Public (registration, login)
  - All other paths → Authenticated users
- **Method-Level**: `@PreAuthorize` annotations in services:
  - `@PreAuthorize("hasRole('ADMIN')")`
  - `@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")`
  - `@PreAuthorize("#userId==principal.id")` (user can only access their own data)
  - `@PreAuthorize("@auth.canAccessOrder(#orderID)")` (custom authorization logic)

### Secured vs Public Endpoints

**Public Endpoints** (No Authentication):
- `POST /auth/user/register` - User registration
- `POST /auth/user/jwt/login` - JWT login
- `GET /swagger-ui/**` - Swagger documentation
- `GET /v3/api-docs/**` - OpenAPI JSON

**Authenticated Endpoints** (Require Valid JWT):
- All product browsing, search, category endpoints
- Cart operations
- Order placement and tracking
- Review submission
- Return requests
- User profile management

**Role-Specific Endpoints**:
- Admin-only: All `/admin/**` endpoints, return policy management
- Admin/Manager: Product creation, inventory management, AI features
- User-specific: Users can only access their own cart, orders, reviews

---

## 5. MODULE-WISE BREAKDOWN

### User Module

**Purpose**: User account management, profile updates, password changes

**Key APIs**:
- `PATCH /user/profile` - Update user profile (name, email)
- `PUT /user/update/{username}/password` - Change password

**Business Logic**:
- Profile updates require username validation
- Password changes require current password verification (via Spring Security)
- Users can only update their own profiles (`#userId==principal.id`)

**Role Access**: Authenticated users (own profile only), Admin (all users)

---

### Authentication Module

**Purpose**: User registration, JWT-based login, current user information

**Key APIs**:
- `POST /auth/user/register` - Register new user
- `POST /auth/user/jwt/login` - Authenticate and receive JWT token
- `GET /auth/loggedIn/me` - Get current logged-in user details

**Business Logic**:
- Registration: Password hashing (BCrypt), default role assignment (ROLE_USER), email/username uniqueness validation
- Login: Credential validation, JWT token generation (24-hour expiration), session creation
- Current User: Extracts user from SecurityContext (JWT token)

**Role Access**: Public (registration, login), Authenticated (current user)

---

### Product Module

**Purpose**: Complete product lifecycle management with AI integration and image handling

**Key APIs**:
- `POST /product` - Create product (ADMIN/MANAGER)
- `GET /product/{id}` - Get product by ID
- `GET /product` - Get all products (paginated)
- `GET /product/category/{name}` - Get products by category
- `GET /product/search?keyword=...` - Search products
- `PATCH /product/{id}` - Update product
- `DELETE /product/{id}` - Delete product (ADMIN only)
- `PUT /product/{id}/price` - Update price
- `PUT /product/{id}/discount` - Update discount
- `PUT /product/{id}/update-description-ai` - **AI-generated description**
- `PUT /product/{id}/upload-image` - **Cloudinary image upload**

**Business Logic**:
- Product creation requires category association
- SKU generation (unique product identifier)
- Stock validation before operations
- Price/discount calculations with validation (discount 0-100%)
- **AI Integration**: Calls `AiSer.rewriteDescription()` to generate/improve product descriptions
- **Image Upload**: Validates file type (JPEG, PNG, WEBP), size (max 5MB), uploads to Cloudinary, stores URL and publicId
- Search uses case-insensitive matching on name and description
- Pagination with size limits (max 100 items per page)

**Role Access**: 
- Browse/Search: Authenticated users
- Create/Update/Delete: ADMIN or MANAGER
- AI/Image Upload: ADMIN or MANAGER

---

### Category Module

**Purpose**: Product categorization with return policy associations

**Key APIs**:
- `POST /category` - Create category (ADMIN/MANAGER)
- `GET /category/{id}` - Get category by ID
- `GET /category/by-name/{name}` - Get category by name
- `GET /category/getAll` - Get all categories (paginated)
- `PUT /category/{id}` - Update category
- `DELETE /category/{id}` - Delete category
- `PUT /category/{id}/policy/{policyId}` - Assign return policy to category

**Business Logic**:
- Category creation requires unique name
- Each category must have a return policy assigned
- Policy assignment links category to return policy (defines return window and capabilities)
- Category deletion checks for associated products

**Role Access**: 
- View: Authenticated users
- Create/Update/Delete: ADMIN or MANAGER

---

### Cart Module

**Purpose**: Shopping cart management with automatic total calculation

**Key APIs**:
- `GET /cart/user/{userId}/cart` - Get user's cart
- `PUT /cart/users/{userId}/cart/items` - Add item to cart
- `PUT /cart/{userId}/cart/items/{productId}` - Update item quantity
- `DELETE /cart/{userId}/cart/items/{productId}` - Remove item from cart
- `DELETE /cart/{userId}/cart` - Clear entire cart

**Business Logic**:
- Cart is created automatically on first item addition (One-to-One with User)
- Stock validation before adding items (checks availability and quantity)
- Price calculation includes discounts (via `PriceCalculator`)
- Cart total is recalculated automatically on item changes
- Cart uses `Map<Long, CartItem>` for efficient item lookup by productId
- Users can only access their own cart (`#userId==principal.id`)

**Role Access**: Authenticated users (own cart only), Admin (any user's cart for support)

---

### Order Module

**Purpose**: Order placement, tracking, cancellation, and advanced filtering

**Key APIs**:
- `POST /order/users/{userId}/orders` - Place order from cart
- `GET /order/orders/{orderID}` - Get order by ID
- `GET /order/users/{userId}/orders` - Get user's orders (paginated)
- `GET /order?mode=...&fromDate=...&toDate=...` - **Advanced filtering**
- `PUT /order/orders/{orderId}/cancel` - Cancel order
- `PUT /order/{orderId}/status` - Update order status (ADMIN)

**Business Logic**:
- **Order Placement**:
  - Validates cart is not empty
  - Checks product availability and stock sufficiency
  - Calculates total with discounts applied
  - Reduces product stock immediately
  - Creates order with PENDING status
  - Generates unique tracking number
  - Clears cart after successful order
  - Associates return policy from category to each order item
- **Order Status Flow**: PENDING → PROCESSING → SHIPPED → DELIVERED → CANCELLED
- **Cancellation**: Only allowed for PENDING or PROCESSING orders
- **Advanced Filtering**: Uses JPA Specifications (`OrderSpecifications`) for:
  - Payment mode (CASH, CARD, UPI, etc.)
  - Date range (fromDate, toDate)
  - Order status
  - Amount range (minimumAmount, maxAmount)
  - Combined filters with pagination
- **Tracking**: Unique tracking number generated for each order

**Role Access**: 
- Place/View own orders: Authenticated users
- View all orders with filters: Authenticated users
- Update status: ADMIN only

---

### Return & Return Policy Module

**Purpose**: Comprehensive return request system with policy enforcement and refund calculations

**Key APIs**:
- `POST /return?userId=...` - Raise return request
- `GET /return/{returnId}?userId=...` - Get return request details
- `POST /return/policy/return/policy` - Create return policy (ADMIN)
- `GET /return/policy/return/policy/{policyId}` - Get policy
- `PATCH /return/policy/return/policy/{policyId}` - Update policy
- `PATCH /return/policy/return/policy/{policyId}/activate` - Activate policy
- `PATCH /return/policy/return/policy/{policyId}/deactivate` - Deactivate policy
- `PUT /admin/return/approveReturnRequest` - Approve return (ADMIN)
- `PUT /admin/return/rejectReturnRequest` - Reject return (ADMIN)
- `PUT /admin/return/completeReturnRequest` - Complete return (ADMIN)

**Business Logic**:
- **Return Request Validation**:
  - Order item must belong to user
  - Item must be returnable (policy capability not NONE)
  - Return window must not be expired (days since delivery <= windowSize)
  - No active return request exists for the item
  - Order must be delivered
- **Return Types**: REFUND, REPLACEMENT (based on policy capabilities)
- **Policy Capabilities**:
  - `NONE`: Item not returnable
  - `REFUND_ONLY`: Only refund allowed
  - `REPLACEMENT_ONLY`: Only replacement allowed
  - `BOTH`: Both refund and replacement allowed
- **Return Window**: Configurable per policy (0-16 days from delivery)
- **Refund Calculation** (`RefundCalculator`):
  - Base refund = item price × return quantity
  - Restocking fee = base refund × fee percentage (default: 5%)
  - Final refund = base refund - restocking fee
- **Return Status Flow**: REQUESTED → APPROVED/REJECTED → COMPLETED
- **Policy Management**: Policies can be activated/deactivated, assigned to categories

**Role Access**: 
- Raise return: Authenticated users (own orders)
- View return: Authenticated users (own returns)
- Policy management: ADMIN only
- Return approvals: ADMIN only

---

### Inventory Module

**Purpose**: Stock management and product availability control

**Key APIs**:
- `PATCH /inventory/{productId}/reduce-stock?quantity=...` - Reduce stock
- `PATCH /inventory/{productId}/increase-stock?quantity=...` - Increase stock
- `PATCH /inventory/{productId}/availability?available=true/false` - Set availability

**Business Logic**:
- Stock reduction validates sufficient quantity available
- Stock increase adds to existing quantity
- Availability toggle allows manual product enable/disable
- Stock updates are transactional (prevents race conditions)
- Used during order placement (automatic stock reduction)

**Role Access**: ADMIN or MANAGER

---

### Review Module

**Purpose**: Product reviews and ratings with validation

**Key APIs**:
- `POST /review/products/{productId}/reviews` - Add review
- `PATCH /review/reviews/{reviewId}` - Update review
- `DELETE /review/reviews/{reviewId}` - Delete review
- `GET /review/products/{productId}/reviews` - Get product reviews (paginated)
- `GET /review/users/{userId}/reviews` - Get user's reviews (paginated)
- `GET /review/products/{productId}/rating` - Get average product rating

**Business Logic**:
- **Review Validation**:
  - User must have purchased and received the product (order status: DELIVERED)
  - One review per product per user (prevents duplicates)
  - Rating must be between 1-5
- **Average Rating**: Calculated from all reviews for a product
- **Review Ownership**: Users can only update/delete their own reviews
- **Pagination**: Reviews are paginated for performance

**Role Access**: Authenticated users (own reviews), View: All authenticated users

---

### Admin Module

**Purpose**: System administration, user management, return approvals

**Key APIs**:
- `PATCH /admin/{username}/status?enabled=true/false` - Enable/disable user
- `PUT /admin/user/{username}/role?roleEnum=...` - Update user role
- `GET /admin/user/getAll` - Get all users (paginated)
- `GET /admin/user/getUser?value=...&type=USERNAME/EMAIL/ID` - Search user
- `PUT /admin/return/approveReturnRequest?returnId=...` - Approve return
- `PUT /admin/return/rejectReturnRequest?returnId=...` - Reject return
- `PUT /admin/return/completeReturnRequest?returnId=...` - Complete return
- `GET /admin/return/getPendingReturnRequests` - Get pending returns (paginated)

**Business Logic**:
- **User Management**:
  - Account enable/disable (prevents login when disabled)
  - Role assignment (ADMIN, MANAGER, USER)
  - User search by username, email, or ID
- **Return Management**:
  - Approve/reject return requests
  - Complete approved returns (process refund/replacement)
  - View pending returns with pagination

**Role Access**: ADMIN only

---

### AI-Powered Features Module

**Purpose**: AI-generated product descriptions using Spring AI and Ollama

**Key Functionality**:
- **Product Description Generation** (`AiSer.rewriteDescription()`):
  - Takes product name, category, and current description
  - Sends prompt to Ollama via Spring AI ChatClient
  - Uses `qwen2.5:0.5b` model for generation
  - Generates professional, engaging descriptions (max 500 characters)
  - Returns rewritten description
- **RAG Support**: Includes Retrieval Augmented Generation with query transformers:
  - `RewriteQueryTransformer`: Improves prompt clarity
  - `TranslationQueryTransformer`: Ensures English output
- **Integration**: Called from `ProductSer.updateProductDescriptionWithAi()`

**Business Logic**:
- Only ADMIN/MANAGER can trigger AI description generation
- Validates product exists before AI call
- Updates product description in database after generation
- Handles AI service failures gracefully

**AI Service Configuration**:
- Ollama base URL: Configurable via `OLLAMA_BASE_URL` (default: `http://localhost:11434`)
- Model: `qwen2.5:0.5b` (lightweight, fast generation)
- System prompt: "You are a professional copywriter"
- User prompt: Includes product details and requirements

**Is AI Optional or Required?**
- **Optional**: The system functions without AI (manual descriptions)
- AI is an **enhancement feature** for product managers
- If Ollama is unavailable, the endpoint returns an error (does not break the system)

---

## 6. API DOCUMENTATION STRATEGY

### Swagger/OpenAPI Configuration

**Configuration Class** (`SwaggerApiConfig`):
- Defines OpenAPI 3.0 specification
- Configures API metadata (title, version, description)
- Sets up JWT security scheme (`bearerAuth`)
- Adds security requirement to all endpoints by default

**Security Scheme**:
- Type: HTTP Bearer Authentication
- Scheme: `bearer`
- Bearer Format: JWT
- Header Name: `Authorization`

### Security Representation in Swagger

- **Public Endpoints**: Annotated with `@SecurityRequirement(name = "")` to override default security
- **Secured Endpoints**: Inherit default security requirement (JWT token required)
- **Swagger UI**: Provides "Authorize" button to input JWT token
- **Token Testing**: Users can test secured endpoints directly from Swagger UI

### Tag Grouping and Endpoint Visibility

**Controller Tags** (11 tags):
- `Authentication` - Auth endpoints
- `Product` - Product management
- `Category` - Category management
- `Cart` - Shopping cart
- `Order` - Order management
- `Review` - Reviews and ratings
- `User` - User profile
- `Admin` - Admin operations
- `Return` - Return requests
- `Return Policy` - Policy management
- `Inventory` - Stock management

**Endpoint Visibility**:
- All endpoints are visible in Swagger UI
- `@Hidden` annotation can hide specific endpoints (e.g., session-based login)
- Each endpoint includes:
  - Summary and description
  - Request/response schemas
  - Parameter descriptions
  - Example values
  - HTTP status codes

---

## 7. CONFIGURATION MANAGEMENT

### application.yml Usage

**Structure**: YAML-based configuration with environment variable placeholders

**Sections**:
1. **Server Configuration**:
   - Port: `${SERVER_PORT:8081}` (default: 8081)
   - Context Path: `${SERVER_CONTEXT_PATH:/backend/v1}` (default: /backend/v1)

2. **Database Configuration**:
   - URL: `${DB_URL}` (required)
   - Username: `${DB_USERNAME}` (required)
   - Password: `${DB_PASSWORD}` (required)
   - JPA: Hibernate ddl-auto: update, show-sql: true

3. **Spring AI Configuration**:
   - Ollama base URL: `${OLLAMA_BASE_URL}` (required for AI features)

4. **JWT Configuration**:
   - Secret Key: `${JWT_SECRET_KEY:myKey1234}` (default provided, should be overridden)

5. **Cloudinary Configuration**:
   - Cloud Name: `${CLOUDINARY_CLOUD_NAME}` (required)
   - API Key: `${CLOUDINARY_API_KEY}` (required)
   - API Secret: `${CLOUDINARY_API_SECRET}` (required)

6. **Default Admin**:
   - Username: `${ADMIN_USERNAME}` (required)
   - Password: `${ADMIN_PASSWORD}` (required)
   - Email: `${ADMIN_EMAIL}` (required)

### Environment Variables Strategy

**.env File** (gitignored):
- Contains all sensitive configuration
- Loaded via `spring.config.import: optional:file:.env`
- Format: `KEY=value` (one per line)
- Example:
  ```
  DB_URL=jdbc:mysql://localhost:3306/ecommercebackend
  DB_USERNAME=root
  DB_PASSWORD=secret
  JWT_SECRET_KEY=very-secure-secret-key-minimum-64-characters
  CLOUDINARY_CLOUD_NAME=your_cloud_name
  CLOUDINARY_API_KEY=your_api_key
  CLOUDINARY_API_SECRET=your_api_secret
  OLLAMA_BASE_URL=http://localhost:11434
  ADMIN_USERNAME=admin
  ADMIN_PASSWORD=admin@123
  ADMIN_EMAIL=admin@example.com
  ```

### Secrets Handling

**Best Practices**:
- No secrets hardcoded in source code
- All secrets externalized to environment variables
- `.env` file is gitignored (never committed)
- Default values provided only for non-sensitive configs (e.g., port, context path)
- Production: Use environment variables or secret management systems (AWS Secrets Manager, HashiCorp Vault)

### Docker Compatibility

**Dockerfile Configuration**:
- Uses environment variables for all configuration
- No hardcoded values in Dockerfile
- Supports `.env` file via `docker-compose.yml` or `--env-file` flag
- Example Docker run:
  ```bash
  docker run -e DB_URL=... -e JWT_SECRET_KEY=... ...
  ```

**docker-compose.yml Integration**:
- Can use `env_file: .env` to load all variables
- Supports environment variable overrides
- Secrets can be passed via Docker secrets or environment variables

---

## 8. DATABASE DESIGN

### Major Entities

1. **UserEn**: Users of the system
   - Fields: id, username (unique), email (unique), password (hashed), name, role, isEnabled
   - Relationships: One-to-One with Cart, One-to-Many with Orders, One-to-Many with Reviews

2. **ProductEn**: Products in catalog
   - Fields: id, name, description, price, quantity (stock), availability, sku (unique), imageUrl, discount, publicId (Cloudinary)
   - Relationships: Many-to-One with Category, One-to-Many with OrderItems, CartItems, Reviews

3. **CategoryEn**: Product categories
   - Fields: id, name (unique), description, imageUrl
   - Relationships: One-to-Many with Products, Many-to-One with ReturnPolicy

4. **OrderEn**: Customer orders
   - Fields: id, totalAmount, address, trackingNumber (unique), totalItems, orderStatus, paymentMode, createdAt, deliveredAt
   - Relationships: Many-to-One with User, One-to-Many with OrderItems

5. **OrderItem**: Items within an order
   - Fields: id, quantity, price (at time of order), returnCapabilities, windowSize
   - Relationships: Many-to-One with Order, Many-to-One with Product

6. **CartEn**: Shopping carts
   - Fields: id, totalAmount
   - Relationships: One-to-One with User, One-to-Many with CartItems (stored as Map)

7. **CartItem**: Items in cart
   - Fields: id, quantity, price
   - Relationships: Many-to-One with Cart, Many-to-One with Product

8. **ReviewEn**: Product reviews
   - Fields: id, rating (1-5), description, createdAt
   - Relationships: Many-to-One with User, Many-to-One with Product

9. **ReturnEn**: Return requests
   - Fields: id, reason, returnTypeEnum, returnStatusEnum, requestedAt, refundableAmount
   - Relationships: Many-to-One with User, Many-to-One with OrderItem, One-to-One with RefundPayment

10. **ReturnPolicyEn**: Return policies
    - Fields: id, policyName (unique), capabilities (enum), windowSize (0-16 days), active
    - Relationships: One-to-Many with Categories

11. **RefundPaymentEn**: Refund payment records
    - Fields: id, refundAmount, refundMode, refundStatus
    - Relationships: One-to-One with Return

### Entity Relationships Rationale

**Why These Mappings?**
- **One-to-One (User ↔ Cart)**: Each user has exactly one cart (simplifies cart management)
- **One-to-Many (User → Orders)**: Users place multiple orders over time
- **Many-to-One (Product → Category)**: Products belong to one category (hierarchical organization)
- **One-to-Many (Order → OrderItems)**: Orders contain multiple items (normalized design)
- **Many-to-One (Category → ReturnPolicy)**: Categories share return policies (policy reuse)
- **One-to-One (OrderItem → Return)**: Each return request is for one specific order item (granular returns)

**Cascade Strategy**:
- `CascadeType.ALL` used for parent-child relationships (e.g., Order → OrderItems)
- Ensures data consistency (deleting order deletes items)
- `orphanRemoval = true` automatically removes children when parent is deleted

**Lazy Loading**:
- `FetchType.LAZY` for all One-to-Many and Many-to-One relationships
- Prevents N+1 query problems
- Improves performance by loading related entities only when accessed

---

## 9. ERROR HANDLING & VALIDATION

### GlobalExceptionHandler Strategy

**Centralized Exception Handling**:
- Single `@RestControllerAdvice` class handles all exceptions
- Returns uniform `ErrorResponseDto` structure
- Logs all exceptions for debugging
- Maps exceptions to appropriate HTTP status codes

**Exception Mapping**:
- `ResourceNotFoundException` → 404 Not Found
- `InvalidInputException` → 400 Bad Request
- `AccessDeniedException` → 403 Forbidden
- `DuplicationEntryException` → 400 Bad Request
- `InSufficientStockException` → 400 Bad Request
- `AlreadyReviewedException` → 400 Bad Request
- `ReturnWindowExpirationException` → 400 Bad Request
- `MethodArgumentNotValidException` → 400 with field errors map
- Generic `Exception` → 500 Internal Server Error

### Validation Errors

**Jakarta Validation**:
- Applied via annotations on DTOs and entities:
  - `@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@PositiveOrZero`, `@Min`, `@Max`
- Validation occurs at controller level (before service execution)
- Invalid input returns 400 with field-level error details:
  ```json
  {
    "fieldName": "Validation error message",
    "anotherField": "Another error message"
  }
  ```

**Custom Validation**:
- Business logic validations in service layer:
  - Stock sufficiency checks
  - Return window validation
  - Duplicate review prevention
  - Order ownership verification

### Standard Error Response Format

**ErrorResponseDto Structure**:
```json
{
  "message": "Human-readable error description",
  "errorCode": "HTTP_STATUS_CODE (e.g., NOT_FOUND, BAD_REQUEST)",
  "timeStamp": "2024-01-01T12:00:00"
}
```

**Success Response Format**:
```json
{
  "message": "Success message",
  "status": "SUCCESS",
  "time": "2024-01-01T12:00:00"
}
```

**Benefits**:
- Consistent error format across all endpoints
- Easy client-side error handling
- Timestamp for debugging and logging
- Clear error codes for programmatic handling

---

## 10. AI INTEGRATION (IMPORTANT)

### Why Spring AI + Ollama?

**Spring AI Framework**:
- Provides abstraction layer for AI model integration
- Supports multiple AI providers (OpenAI, Ollama, etc.)
- Built-in RAG (Retrieval Augmented Generation) support
- Easy prompt engineering and response handling

**Ollama (Local AI)**:
- **Privacy**: Runs locally, no data sent to external services
- **Cost**: Free, no API costs
- **Control**: Full control over models and configuration
- **Speed**: Low latency (local network)
- **Offline**: Works without internet connection

### Where AI is Used

**Primary Use Case: Product Description Generation**
- Endpoint: `PUT /product/{productId}/update-description-ai`
- Service: `ProductSer.updateProductDescriptionWithAi()`
- AI Service: `AiSer.rewriteDescription()`

**Process Flow**:
1. Manager/Admin triggers AI description generation for a product
2. System sends product details (name, category, current description) to Ollama
3. Ollama generates improved description using `qwen2.5:0.5b` model
4. Generated description is validated (max 500 characters)
5. Product description is updated in database

**AI Prompt**:
```
You are an expert product description writer.

Product Name: {productName}
Category: {categoryName}
Current Description: {currentDescription}

Rewrite the description to make it:
1. Clear, professional, and engaging.
2. Focused on the product's main features.
3. Short enough to not exceed 500 characters (strictly follow this limit).

Return only the rewritten description, do not add anything else.
```

### How AI Improves the Product

**Benefits**:
1. **Time Savings**: Automates manual description writing
2. **Consistency**: Generates professional, consistent descriptions
3. **Quality**: Improves clarity and engagement of product descriptions
4. **Scalability**: Can generate descriptions for large product catalogs quickly
5. **Localization Ready**: Can be extended for multi-language descriptions

**Business Value**:
- Reduces time-to-market for new products
- Improves product listing quality (better SEO, conversions)
- Enables bulk product onboarding with AI assistance

### Is AI Optional or Required?

**AI is OPTIONAL**:
- The system functions completely without AI
- Products can have manual descriptions
- AI endpoint returns error if Ollama is unavailable (does not crash system)
- AI is an **enhancement feature**, not a core dependency

**Configuration**:
- If `OLLAMA_BASE_URL` is not configured, AI features are unavailable
- AI endpoints are protected (ADMIN/MANAGER only)
- System gracefully handles AI service failures

**Future Enhancements**:
- AI could be extended for:
  - Product categorization suggestions
  - Review sentiment analysis
  - Personalized product recommendations
  - Automated customer support responses

---

## 11. PRODUCTION READINESS

### Logging Strategy

**Logging Framework**: SLF4J with Logback (Spring Boot default)

**Logging Levels**:
- **DEBUG**: Spring AI advisor logging (for AI debugging)
- **INFO**: General application flow
- **ERROR**: Exception logging in GlobalExceptionHandler
- **WARN**: Deprecated features, configuration issues

**Logging Points**:
- All exceptions are logged with full stack traces
- Service methods use `LoggerFactory.getLogger()` for important operations
- Security events (login attempts, authorization failures) are logged

**Production Recommendations**:
- Configure log levels via `application.yml` or environment variables
- Use structured logging (JSON format) for log aggregation tools
- Implement log rotation and retention policies
- Integrate with centralized logging (ELK stack, CloudWatch, etc.)

### Security Best Practices

**Implemented**:
1. **Password Security**:
   - BCrypt hashing (strength: 12)
   - Passwords never stored in plain text
   - Passwords not exposed in DTOs

2. **JWT Security**:
   - Tokens signed with HMAC-SHA256
   - 24-hour expiration (configurable)
   - Secret key externalized (not hardcoded)
   - Token validation on every request

3. **Role-Based Access**:
   - URL-level and method-level security
   - Principle of least privilege
   - Users can only access their own data

4. **Input Validation**:
   - Jakarta Validation on all inputs
   - SQL injection prevention (JPA parameterized queries)
   - XSS prevention (Spring's default encoding)

5. **Non-Root Execution**:
   - Docker container runs as non-root user
   - Reduces attack surface

**Recommendations for Production**:
- Use HTTPS only (TLS/SSL)
- Implement rate limiting (prevent brute force)
- Add CORS configuration for frontend domains
- Use strong JWT secret (minimum 64 characters, randomly generated)
- Implement token refresh mechanism
- Add security headers (HSTS, CSP, etc.)
- Regular security audits and dependency updates

### Scalability Considerations

**Current Architecture Supports**:
- **Horizontal Scaling**: Stateless JWT authentication allows multiple instances
- **Database Connection Pooling**: Spring Boot's HikariCP (default)
- **Pagination**: All list endpoints support pagination (prevents large data loads)
- **Lazy Loading**: Optimized database queries

**Scalability Improvements Needed**:
1. **Caching**: Implement Redis for:
   - Product catalog caching
   - User session caching (if needed)
   - Frequently accessed data

2. **Database Optimization**:
   - Add database indexes on frequently queried fields
   - Implement read replicas for read-heavy operations
   - Consider database sharding for very large datasets

3. **Load Balancing**:
   - Use load balancer (Nginx, AWS ALB) for multiple instances
   - Session affinity not required (stateless JWT)

4. **Message Queue**:
   - For async operations (email notifications, order processing)
   - Use RabbitMQ or Apache Kafka

5. **CDN**:
   - Serve static assets (product images) via CDN
   - Cloudinary already provides CDN

### Config Externalization

**Fully Externalized**:
- All configuration in `application.yml` uses environment variables
- No hardcoded secrets or credentials
- Supports multiple environments (dev, staging, prod)

**Docker Ready**:
- Dockerfile accepts all config via environment variables
- `.env` file support for local development
- Production: Use environment variables or secret management

### Docker Readiness

**Production-Ready Dockerfile**:
- Multi-stage build (smaller final image)
- Non-root user execution
- JVM optimizations for containers
- Health check support (commented, ready to enable)
- Proper signal handling (graceful shutdown)

**Deployment Options**:
- Docker Compose (local development)
- Kubernetes (production orchestration)
- Cloud platforms (AWS ECS, Google Cloud Run, Azure Container Apps)

---

## 12. WHAT MAKES THIS PROJECT STRONG

### Key Differentiators

1. **Complete E-Commerce Lifecycle**:
   - Not just CRUD - handles entire order-to-return workflow
   - Complex business logic (return policies, refund calculations, stock management)
   - Real-world scenarios (return windows, order status tracking)

2. **AI Integration**:
   - Modern AI-powered features (product description generation)
   - Local AI (privacy-focused, cost-effective)
   - Extensible for future AI enhancements

3. **Enterprise-Grade Security**:
   - JWT-based stateless authentication
   - Three-tier role-based access control
   - Method-level security with custom authorization logic
   - BCrypt password hashing

4. **Advanced Features**:
   - **Return Management**: Complete return workflow with policy enforcement
   - **Advanced Filtering**: JPA Specifications for complex order queries
   - **Image Management**: Cloudinary integration with CDN
   - **Review System**: Validated reviews (only purchasers can review)

5. **Production-Ready Architecture**:
   - Layered architecture with clear separation
   - DTO pattern for API stability
   - Global exception handling
   - Comprehensive validation
   - Docker containerization

6. **Developer Experience**:
   - Complete Swagger/OpenAPI documentation
   - Interactive API testing
   - Clear error messages
   - Comprehensive logging

### Why This is NOT a Simple CRUD App

**Complex Business Logic**:
- **Order Processing**: Stock validation, price calculation with discounts, cart clearing, tracking number generation
- **Return Management**: Policy validation, window expiration checks, refund calculations with restocking fees, status workflow
- **Review Validation**: Purchase verification, duplicate prevention, average rating calculation
- **Cart Management**: Automatic total calculation, stock checks, discount application
- **Inventory Management**: Real-time stock updates, availability toggling

**Advanced Patterns**:
- **JPA Specifications**: Dynamic query building for order filtering
- **Custom Authorization**: SpEL expressions for complex access control (`@auth.canAccessOrder()`)
- **Transaction Management**: Ensures data consistency across operations
- **Utility Classes**: Reusable business logic (PriceCalculator, RefundCalculator, ReturnPolicyResolver)

**Real-World Scenarios**:
- Handles edge cases (insufficient stock, expired return windows, duplicate reviews)
- Enforces business rules (return policies, review eligibility)
- Supports multiple user roles with different permissions
- Manages complex workflows (order status transitions, return approvals)

### Real-World Relevance

**This system could power**:
- Small to medium e-commerce businesses
- Marketplace platforms
- B2B product catalogs
- Retail inventory management systems

**Industry Standards**:
- Follows REST API best practices
- Implements OAuth2-like JWT authentication
- Uses industry-standard security (BCrypt, JWT)
- Adopts microservices-ready architecture (stateless, containerized)

**Scalability Path**:
- Can be extended to microservices architecture
- Supports horizontal scaling
- Ready for cloud deployment
- Can integrate with payment gateways, shipping providers, etc.

---

## 13. CURRENT LIMITATIONS

### What is Missing for Full Production

1. **Payment Integration**:
   - No actual payment processing (payment mode is stored but not processed)
   - Need integration with payment gateways (Stripe, PayPal, etc.)

2. **Email Notifications**:
   - No email service for order confirmations, return status updates
   - Should integrate with email service (SendGrid, AWS SES, etc.)

3. **Order Shipping**:
   - No shipping provider integration
   - Tracking numbers are generated but not linked to shipping services

4. **Search Functionality**:
   - Basic search (name/description matching)
   - Should implement full-text search (Elasticsearch, Solr) for better results

5. **Caching Layer**:
   - No caching layer (Redis, Memcached)
   - Frequently accessed data (products, categories) should be cached

6. **File Upload Validation**:
   - Basic file type/size validation
   - Should add virus scanning, image optimization

7. **Rate Limiting**:
   - No rate limiting on API endpoints
   - Vulnerable to brute force attacks, DDoS

8. **Monitoring & Observability**:
   - Basic logging only
   - Need APM tools (New Relic, Datadog), metrics (Prometheus), distributed tracing

9. **Database Migrations**:
   - Uses `ddl-auto: update` (not production-safe)
   - Should use Flyway or Liquibase for versioned migrations

10. **API Versioning**:
    - Single API version (`/backend/v1`)
    - Should support multiple versions for backward compatibility

11. **Health Checks**:
    - No health check endpoint (Spring Boot Actuator not fully configured)
    - Needed for load balancers and monitoring

12. **Background Jobs**:
    - No async processing
    - Should add scheduled tasks (order status updates, cleanup jobs)

### Possible Improvements

1. **Performance**:
   - Add database indexes on frequently queried columns
   - Implement query result caching
   - Optimize N+1 query problems (use `@EntityGraph`)

2. **Security**:
   - Implement token refresh mechanism
   - Add CSRF protection for state-changing operations
   - Implement API rate limiting
   - Add security headers (HSTS, CSP)

3. **Features**:
   - Wishlist functionality
   - Product recommendations (AI-powered)
   - Order history with advanced filtering
   - Bulk operations (bulk product upload, bulk order processing)

4. **Testing**:
   - Add integration tests
   - Add API contract tests
   - Add performance tests
   - Increase unit test coverage

5. **Documentation**:
   - Add API versioning documentation
   - Create deployment guides
   - Add troubleshooting documentation
   - Create developer onboarding guide

6. **DevOps**:
   - CI/CD pipeline (GitHub Actions, Jenkins)
   - Automated testing in pipeline
   - Automated deployment
   - Infrastructure as Code (Terraform, CloudFormation)

---

## Conclusion

This AI-Powered E-Commerce Backend is a **production-ready, enterprise-grade Spring Boot application** that demonstrates:

- **Complete system design** with layered architecture
- **Modern technologies** (Java 21, Spring Boot 3.5.9, Spring AI, JWT)
- **Complex business logic** beyond simple CRUD
- **Security best practices** (JWT, RBAC, password hashing)
- **Real-world features** (returns, reviews, inventory, AI)
- **Developer-friendly** (Swagger docs, clear error handling)
- **Scalable architecture** (stateless, containerized, pagination)

The system is ready for deployment with proper environment configuration and can be extended with the improvements listed above for enterprise-scale production use.

---

**Document Version**: 1.0  
**Last Updated**: 2024  
**Technology Stack**: Spring Boot 3.5.9, Java 21, MySQL 8.0+, Spring AI 1.0.0, Cloudinary 1.36.0, JWT 0.13.0
