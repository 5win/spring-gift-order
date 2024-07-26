package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.constants.ErrorMessage;
import gift.domain.KakaoMessageSender;
import gift.domain.KakaoToken;
import gift.dto.KakaoProductMessage;
import gift.dto.OptionSubtractRequest;
import gift.dto.OrderRequest;
import gift.entity.Member;
import gift.entity.Product;
import gift.repository.MemberJpaDao;
import gift.repository.ProductJpaDao;
import gift.repository.WishlistJpaDao;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductJpaDao productJpaDao;
    private final MemberJpaDao memberJpaDao;
    private final WishlistJpaDao wishlistJpaDao;

    private final OptionService optionService;

    private final KakaoMessageSender kakaoSendMessage;

    public OrderService(ProductJpaDao productJpaDao, MemberJpaDao memberJpaDao,
        WishlistJpaDao wishlistJpaDao, OptionService optionService,
        KakaoMessageSender kakaoSendMessage) {
        this.productJpaDao = productJpaDao;
        this.memberJpaDao = memberJpaDao;
        this.wishlistJpaDao = wishlistJpaDao;
        this.optionService = optionService;
        this.kakaoSendMessage = kakaoSendMessage;
    }

    @Transactional
    public void order(OrderRequest orderRequest, String email) throws JsonProcessingException {
        String kakaoToken = KakaoToken.getAccessToken(email);
        Member member = memberJpaDao.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.MEMBER_NOT_EXISTS_MSG));
        Product product = productJpaDao.findById(orderRequest.getProductId())
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.PRODUCT_NOT_EXISTS_MSG));

        kakaoSendMessage.sendForMe(kakaoToken, new KakaoProductMessage(
                product.getName(), product.getImageUrl(), orderRequest.getMessage(),
                product.getImageUrl(), (int) product.getPrice()
            )
        );

        OptionSubtractRequest optionSubtractRequest = new OptionSubtractRequest(orderRequest);
        optionService.subtractOption(optionSubtractRequest);
        // 존재할 때만 지우도록 변경할 것.
//        memberService.deleteWishlist(email, product.getId());
        wishlistJpaDao.findByMember_EmailAndProduct_Id(email, product.getId())
            .ifPresent(wishlist -> {
                wishlistJpaDao.deleteByMember_EmailAndProduct_Id(email, product.getId());
            });

        // order 저장.
    }
}
